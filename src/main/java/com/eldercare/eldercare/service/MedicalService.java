package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalService {

    private final MedicationRepository medicationRepository;
    private final ElderRepository elderRepository;

    @Autowired
    public MedicalService(MedicationRepository medicationRepository,
            ElderRepository elderRepository) {
        this.medicationRepository = medicationRepository;
        this.elderRepository = elderRepository;
    }

    public MedicationSchedule addMedication(MedicationSchedule medication) {
        return medicationRepository.save(medication);
    }

    public List<MedicationSchedule> getAllMedications() {
        return medicationRepository.findAll();
    }

    public MedicationSchedule getMedicationById(String medId) {
        return medicationRepository.findByMedId(medId)
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + medId));
    }

    public MedicationSchedule updateMedication(String medId, MedicationSchedule updatedMedication) {
        return medicationRepository.findByMedId(
                medId)
                .map(med -> {
                    med.setMedicineName(updatedMedication.getMedicineName());
                    med.setDosage(updatedMedication.getDosage());
                    med.setTime(updatedMedication.getTime());
                    return medicationRepository.save(med);
                })
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + medId));
    }

    public void deleteMedication(String medId) {
        MedicationSchedule med = medicationRepository.findByMedId(medId)
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + medId));

        medicationRepository.delete(med);
    }

    public String generatedMedicationId() {
        Optional<MedicationSchedule> last = medicationRepository.findAll()
                .stream()
                .filter(med -> med.getMedId() != null) // ignore null IDs
                .max(Comparator.comparing(MedicationSchedule::getMedId));

        if (last.isPresent()) {
            String lastId = last.get().getMedId();
            int num = Integer.parseInt(lastId.substring(1));
            num++;
            return String.format("M%03d", num);
        } else {
            return "M001"; // first ID if none exist
        }
    }

    public Elder findElderById(String elderId) {
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new RuntimeException("Elder not found with ID: " + elderId));
    }

}
