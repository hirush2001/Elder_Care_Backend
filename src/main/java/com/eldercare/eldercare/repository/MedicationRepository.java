package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.MedicationSchedule;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicationRepository extends JpaRepository<MedicationSchedule, String> {

    Optional<MedicationSchedule> findByMedId(String medId);

    void deleteByMedId(String medId);
}
