package com.eldercare.eldercare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.eldercare.model.CareRequest;
import com.eldercare.eldercare.model.DailyHealthRecord;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CareRequestRepository extends JpaRepository<CareRequest, String> {

    @Query("SELECT cr FROM CareRequest cr JOIN FETCH cr.elder e WHERE cr.careId = :careId")
    List<CareRequest> findByCareIdAndFetchElder(@Param("careId") String careId);

    List<CareRequest> findAllByElder_ElderId(String elderId);

}
