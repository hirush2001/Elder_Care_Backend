package com.eldercare.eldercare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.eldercare.eldercare.model.DailyHealthRecord;

@Repository
public interface HealthRecordRepository extends MongoRepository<DailyHealthRecord, String> {

}
