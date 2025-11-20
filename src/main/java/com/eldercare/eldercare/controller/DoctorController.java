package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.Doctor;
import com.eldercare.eldercare.model.DoctorAvailability;
import com.eldercare.eldercare.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
  private final DoctorService doctorService;

  public DoctorController(DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @PostMapping
  public ResponseEntity<?> addDoctor(@RequestBody Doctor d) {
    return ResponseEntity.ok(doctorService.save(d));
  }

  @PostMapping("/{id}/availability")
  public ResponseEntity<?> addAvailability(@PathVariable String id, @RequestBody DoctorAvailability av) {
    av.setDoctor(doctorService.findById(id).orElseThrow());
    return ResponseEntity.ok(doctorService.addAvailability(av));
  }

  @GetMapping("/{id}/availability")
  public ResponseEntity<List<DoctorAvailability>> getAvailability(@PathVariable String id) {
    return ResponseEntity.ok(doctorService.getAvailability(id));
  }
}
