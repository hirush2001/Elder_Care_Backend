package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalService {

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicalService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public MedicationSchedule addMedication(MedicationSchedule medication) {
        return medicationRepository.save(medication);
    }

    public List<MedicationSchedule> getAllMedications() {
        return medicationRepository.findAll();
    }

    public MedicationSchedule getMedicationById(String id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + id));
    }

    public MedicationSchedule updateMedication(String id, MedicationSchedule updatedMedication) {
        return medicationRepository.findById(id)
                .map(med -> {
                    med.setMedicineName(updatedMedication.getMedicineName());
                    med.setDosage(updatedMedication.getDosage());
                    med.setTime(updatedMedication.getTime());
                    return medicationRepository.save(med);
                })
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + id));
    }

    public void deleteMedication(String id) {
        medicationRepository.deleteById(id);
    }
}
