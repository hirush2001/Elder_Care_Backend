package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.MedicationSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicationRepository extends MongoRepository<MedicationSchedule, String> {
}
