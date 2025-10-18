package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MedicationReminderService {

    private final MedicationRepository medicationRepository;
    private final EmailService emailService;
    private final ElderRepository elderRepository;

    @Autowired
    public MedicationReminderService(MedicationRepository medicationRepository,
            EmailService emailService,
            ElderRepository elderRepository) {
        this.medicationRepository = medicationRepository;
        this.emailService = emailService;
        this.elderRepository = elderRepository;
    }

    // Find Elder by email
    public Elder findElderByEmail(String email) {
        return elderRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Elder not found"));
    }

    @Scheduled(fixedRate = 60000) // every minute
    public void sendReminders() {
        System.out.println("sendReminders() triggered at " + LocalTime.now());

        List<MedicationSchedule> medications = medicationRepository.findAll();
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        for (MedicationSchedule med : medications) {
            LocalTime medTime = med.getTimeAsLocalTime().truncatedTo(ChronoUnit.MINUTES);

            // Check if medication time is now
            if (!medTime.isBefore(now) && medTime.isBefore(now.plusMinutes(1))) {

                // Get Elder email from the database
                Elder elder = findElderByEmail(med.getEldermail());

                String subject = "💊 Medication Reminder";
                String message = "Hello " + elder.getEmail() + "! It's time to take your medicine: " +
                        med.getMedicineName() +
                        "\nDosage: " + med.getDosage() +
                        "\nTime: " + med.getTime();

                emailService.sendEmail(elder.getEmail(), subject, message);
                System.out.println("Reminder sent to: " + elder.getEmail());
            }
        }
    }
}
