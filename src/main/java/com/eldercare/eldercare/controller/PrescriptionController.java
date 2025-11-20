package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.model.Prescription;
import com.eldercare.eldercare.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
  private final PrescriptionService prescriptionService;

  public PrescriptionController(PrescriptionService prescriptionService) {
    this.prescriptionService = prescriptionService;
  }

  // Doctor adds prescription after visit
  @PostMapping("/add")
  public ResponseEntity<?> add(@RequestParam String doctorId,
                               @RequestParam String elderId,
                               @RequestBody PrescriptionRequest req) {
    // convert request meds to MedicationSchedule entities
    List<MedicationSchedule> meds = req.getMedicationSchedules();
    Prescription p = prescriptionService.addPrescription(doctorId, elderId, req.getNotes(), meds);
    return ResponseEntity.ok(p);
  }

  @GetMapping("/elder/{elderId}")
  public ResponseEntity<List<Prescription>> getForElder(@PathVariable String elderId) {
    return ResponseEntity.ok(prescriptionService.getForElder(elderId));
  }

  public static class PrescriptionRequest {
    private String notes;
    private List<MedicationSchedule> MedicationSchedules;
    // getters/setters
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<MedicationSchedule> getMedicationSchedules() { return MedicationSchedules; }
    public void setMedicationSchedules(List<MedicationSchedule> MedicationSchedules) { this.MedicationSchedules = MedicationSchedules; }
  }
}
