
package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MedicationReminderService {

    @Autowired
    private ElderRepository elderRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Map to store scheduled reminders: elderId -> list of medication schedules
    private final ConcurrentHashMap<String, List<MedicationSchedule>> remindersMap = new ConcurrentHashMap<>();

    // ✅ Schedule a new reminder when a medication is added
    public void scheduleReminder(MedicationSchedule medicationSchedule, String elderId) {
        remindersMap.putIfAbsent(elderId, new ArrayList<>());
        remindersMap.get(elderId).add(medicationSchedule);

        System.out.println("Reminder scheduled for elder ID: " + elderId + " at " + medicationSchedule.getTime());
    }

    // ✅ Send a manual reminder
    public void sendReminderToSpecificElder(String elderId) {
        Elder elder = elderRepository.findByElderId(elderId)
                .orElseThrow(() -> new RuntimeException("Elder not found for ID: " + elderId));

        List<MedicationSchedule> meds = remindersMap.get(elderId);
        if (meds == null || meds.isEmpty()) {
            System.out.println("No scheduled medications for elder ID: " + elderId);
            return;
        }

        for (MedicationSchedule med : meds) {
            sendEmail(elder.getEmail(), med);
        }
    }

    // ✅ Scheduled task that runs every minute to check reminders
    @Scheduled(fixedRate = 60000) // every 1 minute
    public void sendAutomaticReminders() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0); // current time in HH:mm format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        remindersMap.forEach((elderId, meds) -> {
            Elder elder = elderRepository.findByElderId(elderId).orElse(null);
            if (elder == null)
                return;

            for (MedicationSchedule med : meds) {
                try {
                    LocalTime medTime = LocalTime.parse(med.getTime(), formatter);
                    if (now.equals(medTime)) {
                        sendEmail(elder.getEmail(), med);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid time format for medication: " + med.getTime());
                }
            }
        });
    }

    // ✅ Helper method to send email
    private void sendEmail(String email, MedicationSchedule med) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("💊 Medication Reminder");
        message.setText("Hello " + email + ",\n\nIt's time to take your medicine:\n" +
                "Medicine: " + med.getMedicineName() +
                "\nDosage: " + med.getDosage() +
                "\nTime: " + med.getTime() +
                "\n\nStay healthy!");

        mailSender.send(message);
        System.out.println("📧 Reminder sent to " + email + " for medicine " + med.getMedicineName());
    }
}
