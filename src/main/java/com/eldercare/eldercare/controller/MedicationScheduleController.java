package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.service.MedicalService;
import com.eldercare.eldercare.service.MedicationReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical")
@CrossOrigin(origins = "*")
public class MedicationScheduleController {

    private final MedicalService medicalService;
    private final MedicationReminderService reminderService;

    @Autowired
    public MedicationScheduleController(MedicalService medicalService,
            MedicationReminderService reminderService) {
        this.medicalService = medicalService;
        this.reminderService = reminderService;
    }

    // ✅ Add a new medicine schedule for a specific elder
    @PostMapping("/addmedicine/{elderId}")
    public String addMedicine(@PathVariable String elderId,
            @RequestBody MedicationSchedule medicationSchedule) {

        String newMedId = medicalService.generatedMedicationId();

        medicationSchedule.setMedId(newMedId);
        // s
        // Save the medication schedule
        medicalService.addMedication(medicationSchedule);

        // Schedule automatic reminder for this elder
        reminderService.scheduleReminder(medicationSchedule, elderId);

        return "Medication schedule added and reminder scheduled for elder ID: " + elderId;
    }

    // ✅ Get all medication schedules
    @GetMapping("/all")
    public List<MedicationSchedule> getAllMedications() {
        return medicalService.getAllMedications();
    }

    // ✅ Get a specific medication by ID
    @GetMapping("/{medId}")
    public ResponseEntity<?> getMedicationById(@PathVariable String medId) {
        try {
            MedicationSchedule med = medicalService.getMedicationById(medId);
            return ResponseEntity.ok(med);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medication not found with ID: " + medId);
        }
    }

    // ✅ Update a medication schedule
    @PutMapping("/{medId}")
    public ResponseEntity<?> updateMedication(@PathVariable String medId,
            @RequestBody MedicationSchedule updatedMedication) {
        try {
            MedicationSchedule med = medicalService.updateMedication(medId, updatedMedication);
            return ResponseEntity.ok("Medication updated successfully: " + med);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medication not found with ID: " + medId);
        }
    }

    // ✅ Delete a medication schedule
    @DeleteMapping("/{medId}")
    public String deleteMedication(@PathVariable String medId) {
        medicalService.deleteMedication(medId);
        return "Medication with ID " + medId + " has been deleted successfully.";
    }

    // ✅ Trigger reminder manually for a specific elder
    @PostMapping("/sendReminder/{elderId}")
    public String sendReminder(@PathVariable String elderId) {
        reminderService.sendReminderToSpecificElder(elderId);
        return "Reminder email sent successfully for elder ID: " + elderId;
    }
}
