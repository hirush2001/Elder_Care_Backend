package com.eldercare.eldercare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eldercare.eldercare.model.CareGiver;
import com.eldercare.eldercare.repository.CareTakerRepository;

@Service
public class CareTakerService {

    private final CareTakerRepository careTakerRepository;

    public CareTakerService(CareTakerRepository careTakerRepository) {
        this.careTakerRepository = careTakerRepository;
    }

    // ✅ Save new CareGiver
    public CareGiver saveCareGiver(CareGiver caregiver) {
        return careTakerRepository.save(caregiver);
    }

    // ✅ Generate unique CareTaker ID (C001, C002, ...)
    public String generateCareTakerId() {
        List<CareGiver> records = careTakerRepository.findAll();

        if (records.isEmpty()) {
            return "C001";
        }

        CareGiver lastRecord = records.get(records.size() - 1);
        String lastId = lastRecord.getCareId();
        int num = Integer.parseInt(lastId.substring(1)); // remove 'C'
        num++;
        return String.format("C%03d", num);
    }

    // ✅ Get all CareGivers
    public List<CareGiver> getAllCareGivers() {
        return careTakerRepository.findAll();
    }

    // ✅ Get CareGiver by ID
    public Optional<CareGiver> getCareGiverById(String id) {
        return careTakerRepository.findById(id);
    }

    // ✅ Update CareGiver
    public CareGiver updateCareGiver(String id, CareGiver updatedCareGiver) {
        return careTakerRepository.findById(id).map(existing -> {
            existing.setFullname(updatedCareGiver.getFullname());
            existing.setEmail(updatedCareGiver.getEmail());
            existing.setContactNumber(updatedCareGiver.getContactNumber());
            return careTakerRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Caregiver not found with ID: " + id));
    }

    // ✅ Delete CareGiver
    public void deleteCareGiver(String id) {
        if (!careTakerRepository.existsById(id)) {
            throw new RuntimeException("Caregiver not found with ID: " + id);
        }
        careTakerRepository.deleteById(id);
    }
}
