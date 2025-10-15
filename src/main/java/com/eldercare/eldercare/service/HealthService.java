package com.eldercare.eldercare.service;

import org.springframework.stereotype.Service;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.repository.HealthRecordRepository;

import java.util.List;

@Service
public class HealthService {

    private final HealthRecordRepository healthRecordRepository;

    public HealthService(HealthRecordRepository healthRecordRepository) {
        this.healthRecordRepository = healthRecordRepository;
    }

    public DailyHealthRecord create(DailyHealthRecord d) {
        return healthRecordRepository.save(d);
    }
}
