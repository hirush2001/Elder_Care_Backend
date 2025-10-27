package com.eldercare.eldercare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldercare.eldercare.model.CareRequest;

public interface CareRequestRepository extends JpaRepository<CareRequest, String> {

}
