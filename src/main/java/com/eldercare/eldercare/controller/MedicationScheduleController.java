package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.config.JwtUtil;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.service.MedicalService;
import com.eldercare.eldercare.service.MedicationReminderService;

import jakarta.servlet.http.HttpServletRequest;

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
    private final JwtUtil jwtUtil;

    @Autowired
    public MedicationScheduleController(MedicalService medicalService,
            MedicationReminderService reminderService, JwtUtil jwtUtil) {
        this.medicalService = medicalService;
        this.reminderService = reminderService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Add a new medicine schedule for a specific elder
    @PostMapping("/addmedicine")
    public String addMedicine(@RequestBody MedicationSchedule medicationSchedule,
            HttpServletRequest request) {

        // Extract JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Extract elderId from token
        String elderId = jwtUtil.extractElderId(token);
        if (elderId == null) {
            throw new RuntimeException("Invalid token: elderId not found");
        }

        // Assign medication ID
        String newMedId = medicalService.generatedMedicationId();
        medicationSchedule.setMedId(newMedId);

        // Lookup Elder by elderId (so you can set the relationship)
        Elder elder = medicalService.findElderById(elderId); // implement this in service if not present
        medicationSchedule.setElder(elder);

        // Save medication
        medicalService.addMedication(medicationSchedule);

        // Schedule automatic reminder
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
    public ResponseEntity<String> deleteMedication(@PathVariable String medId) {
        try {
            medicalService.deleteMedication(medId);
            return ResponseEntity.ok("Medication with ID " + medId + " has been deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medication not found with ID: " + medId);
        }
    }

    // ✅ Trigger reminder manually for a specific elder
    @PostMapping("/sendReminder/{elderId}")
    public String sendReminder(@PathVariable String elderId) {
        reminderService.sendReminderToSpecificElder(elderId);
        return "Reminder email sent successfully for elder ID: " + elderId;
    }
}
