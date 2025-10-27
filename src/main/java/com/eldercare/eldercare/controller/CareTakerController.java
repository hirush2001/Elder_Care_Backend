package com.eldercare.eldercare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eldercare.eldercare.model.CareGiver;
import com.eldercare.eldercare.service.CareTakerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/caretaker")
public class CareTakerController {

    private final CareTakerService careTakerService;

    public CareTakerController(CareTakerService careTakerService) {
        this.careTakerService = careTakerService;
    }

    // POST - Add new caregiver
    @PostMapping("/add")
    public ResponseEntity<?> addCaretaker(@RequestBody CareGiver careGiver) {
        try {
            String newCareId = careTakerService.generateCareTakerId();
            careGiver.setCareId(newCareId);

            CareGiver saved = careTakerService.saveCareGiver(careGiver);
            return ResponseEntity.ok(" Successfully added caretaker with ID: " + saved.getCareId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error adding caretaker: " + e.getMessage());
        }
    }

    // GET - Get all caregivers
    @GetMapping("/all")
    public ResponseEntity<?> getAllCaretakers() {
        try {
            List<CareGiver> list = careTakerService.getAllCareGivers();
            if (list.isEmpty()) {
                return ResponseEntity.ok(" No caregivers found.");
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error retrieving caregivers: " + e.getMessage());
        }
    }

    // GET - Get caregiver by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCaretakerById(@PathVariable String id) {
        try {
            Optional<CareGiver> careGiver = careTakerService.getCareGiverById(id);
            return careGiver
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body("⚠️ Caregiver not found with ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error fetching caretaker: " + e.getMessage());
        }
    }

    // PUT - Update caregiver
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCaretaker(@PathVariable String id, @RequestBody CareGiver updatedCareGiver) {
        try {
            CareGiver updated = careTakerService.updateCareGiver(id, updatedCareGiver);
            return ResponseEntity.ok("✅ Successfully updated caretaker with ID: " + updated.getCareId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("⚠️ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error updating caretaker: " + e.getMessage());
        }
    }

    // DELETE - Delete caregiver
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCaretaker(@PathVariable String id) {
        try {
            careTakerService.deleteCareGiver(id);
            return ResponseEntity.ok("🗑️ Successfully deleted caretaker with ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("⚠️ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error deleting caretaker: " + e.getMessage());
        }
    }
}
