package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical")
@CrossOrigin(origins = "*") // Allow access from frontend
public class MedicationScheduleController {

    private final MedicalService medicalService;

    @Autowired
    public MedicationScheduleController(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    // ✅ Add a new medicine schedule
    @PostMapping("/addmedicine")
    public MedicationSchedule addMedicine(@RequestBody MedicationSchedule medicationSchedule) {
        return medicalService.addMedication(medicationSchedule);
    }

    // ✅ Get all medication schedules
    @GetMapping("/all")
    public List<MedicationSchedule> getAllMedications() {
        return medicalService.getAllMedications();
    }

    // ✅ Get a medication by ID
    @GetMapping("/{id}")
    public MedicationSchedule getMedicationById(@PathVariable String id) {
        return medicalService.getMedicationById(id);
    }

    // ✅ Update a medication schedule
    @PutMapping("/{id}")
    public MedicationSchedule updateMedication(
            @PathVariable String id,
            @RequestBody MedicationSchedule updatedMedication) {
        return medicalService.updateMedication(id, updatedMedication);
    }

    // ✅ Delete a medication by ID
    @DeleteMapping("/{id}")
    public String deleteMedication(@PathVariable String id) {
        medicalService.deleteMedication(id);
        return "Medication with ID " + id + " has been deleted successfully.";
    }
}
