package com.eldercare.eldercare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.eldercare.eldercare.model.DailyHealthRecord;
import java.util.Optional;
import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<DailyHealthRecord, String> {
    Optional<DailyHealthRecord> findById(String healthId);

    List<DailyHealthRecord> findAllByElder_ElderId(String elderId);

}
