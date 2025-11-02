package com.eldercare.eldercare.service;

import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.repository.HealthRecordRepository;
import com.eldercare.eldercare.controller.DailyHealthREcordController;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.model.Elder;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
public class HealthService {

    private final EmailService emailService;
    private final HealthRecordRepository healthRecordRepository;
    private final ElderRepository elderRepository;

    public HealthService(HealthRecordRepository healthRecordRepository,
            EmailService emailService,
            ElderRepository elderRepository) {
        this.healthRecordRepository = healthRecordRepository;
        this.emailService = emailService;
        this.elderRepository = elderRepository;
    }

    // Save the health record
    public DailyHealthRecord create(DailyHealthRecord d) {
        return healthRecordRepository.save(d);
    }

    // Find Elder by email
    public Elder findElderByEmail(String email) {
        return elderRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Elder not found"));
    }

    // Generate health summary message
    public String healthsummary(DailyHealthRecord record, Elder elder) {
        StringBuilder message = new StringBuilder();
        boolean hasWarning = false;

        if (record.getPressure() > 140) {
            message.append("⚠️ High blood pressure detected!\n");
            hasWarning = true;
        }

        if (record.getSugar() > 120) {
            message.append("⚠️ High blood sugar level detected!\n");
            hasWarning = true;
        }

        if (record.getPulse() > 100) {
            message.append("⚠️ High pulse rate detected!\n");
            hasWarning = true;
        }

        if (record.getTemp() > 37.5) {
            message.append("⚠️ Fever detected!\n");
            hasWarning = true;
        }

        if (!hasWarning) {
            message.append("✅ All readings are normal.");
        } else {
            // Send email to the logged-in elder
            emailService.sendEmail(
                    elder.getEmail(),
                    "⚠️ Health Alert!",
                    message.toString());
        }

        return message.toString();
    }

    public String generateHealthId() {
        List<DailyHealthRecord> records = healthRecordRepository.findAll();

        if (records.isEmpty()) {
            return "H001";
        }

        DailyHealthRecord lastRecord = records.get(records.size() - 1);
        String lastId = lastRecord.getHealthId();

        int num = Integer.parseInt(lastId.substring(1)); // remove 'E'
        num++;
        return String.format("H%03d", num);

    }

    public DailyHealthRecord findById(String healthId) {
        return healthRecordRepository.findById(healthId)
                .orElseThrow(() -> new RuntimeException("Health record with ID " + healthId + " not found"));
    }

    public DailyHealthRecord updateHealthRecord(String healthId, DailyHealthRecord updatedRecord) {
        DailyHealthRecord existingRecord = healthRecordRepository.findById(healthId)
                .orElseThrow(() -> new RuntimeException("Health record with ID " + healthId + " not found"));

        // Update fields
        existingRecord.setPressure(updatedRecord.getPressure());
        existingRecord.setSugar(updatedRecord.getSugar());
        existingRecord.setPulse(updatedRecord.getPulse());
        existingRecord.setTemp(updatedRecord.getTemp());

        // Save updated record
        return healthRecordRepository.save(existingRecord);
    }

    public boolean existsById(String healthId) {
        return healthRecordRepository.existsById(healthId);
    }

    public void deleteById(String healthId) {
        healthRecordRepository.deleteById(healthId);
    }

    public List<DailyHealthRecord> findAllByElderId(String elderId) {
        return healthRecordRepository.findAllByElder_ElderId(elderId);
    }

}