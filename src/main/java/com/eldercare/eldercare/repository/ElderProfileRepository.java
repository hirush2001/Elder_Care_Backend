package com.eldercare.eldercare.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eldercare.eldercare.model.ElderProfile;

@Repository
public interface ElderProfileRepository extends JpaRepository<ElderProfile, String> {

    Optional<ElderProfile> findByRegId(String regId);

    List<ElderProfile> findAllByElder_ElderId(String elderId);

    Optional<ElderProfile> findByElder_ElderId(String elderId);

}