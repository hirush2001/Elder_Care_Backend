package com.eldercare.eldercare.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.eldercare.model.CareTakerReview;
import com.eldercare.eldercare.model.DailyHealthRecord;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CareRequestReviewRepository extends JpaRepository<CareTakerReview, String> {

    @Query("SELECT cr FROM CareTakerReview cr JOIN FETCH cr.elder e WHERE cr.careId = :careId")
    List<CareTakerReview> findByCareIdAndFetchElder(@Param("careId") String careId);

    List<CareTakerReview> findAllByElder_ElderId(String elderId);

}
