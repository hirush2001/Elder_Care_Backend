package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.MedicationSchedule;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<MedicationSchedule, String> {

    Optional<MedicationSchedule> findByMedId(String medId);

    List<MedicationSchedule> findAllByElder_ElderId(String elderId);

    void deleteByMedId(String medId);
}
