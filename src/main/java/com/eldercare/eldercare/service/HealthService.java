package com.eldercare.eldercare.service;

import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.repository.HealthRecordRepository;
import com.eldercare.eldercare.controller.DailyHealthREcordController;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.model.Elder;
import org.springframework.stereotype.Service;

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
}
