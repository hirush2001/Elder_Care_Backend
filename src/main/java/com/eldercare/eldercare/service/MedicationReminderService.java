/* 
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
*/

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
