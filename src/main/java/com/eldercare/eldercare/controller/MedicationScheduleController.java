package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.service.MedicalService;
import com.eldercare.eldercare.service.MedicationReminderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/{id}")
    public MedicationSchedule getMedicationById(@PathVariable String id) {
        return medicalService.getMedicationById(id);
    }

    // ✅ Update a medication schedule
    @PutMapping("/{id}")
    public MedicationSchedule updateMedication(@PathVariable String id,
            @RequestBody MedicationSchedule updatedMedication) {
        return medicalService.updateMedication(id, updatedMedication);
    }

    // ✅ Delete a medication schedule
    @DeleteMapping("/{id}")
    public String deleteMedication(@PathVariable String id) {
        medicalService.deleteMedication(id);
        return "Medication with ID " + id + " has been deleted successfully.";
    }

    // ✅ Trigger reminder manually for a specific elder
    @PostMapping("/sendReminder/{elderId}")
    public String sendReminder(@PathVariable String elderId) {
        reminderService.sendReminderToSpecificElder(elderId);
        return "Reminder email sent successfully for elder ID: " + elderId;
    }
}
