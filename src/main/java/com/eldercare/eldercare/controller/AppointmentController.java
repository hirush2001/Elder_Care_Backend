package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.Appointment;
import com.eldercare.eldercare.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
  private final AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  // Book an appointment
  @PostMapping("/book")
  public ResponseEntity<?> book(@RequestParam String elderId,
                                @RequestParam String doctorId,
                                @RequestParam String dateTime, // ISO format e.g. 2025-11-20T14:30
                                @RequestParam(required = false) String reason) {
    LocalDateTime dt = LocalDateTime.parse(dateTime);
    try {
      Appointment ap = appointmentService.bookAppointment(elderId, doctorId, dt, reason);
      return ResponseEntity.ok(ap);
    } catch (IllegalStateException | IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping("/elder/{elderId}")
  public ResponseEntity<List<Appointment>> getByElder(@PathVariable String elderId) {
    return ResponseEntity.ok(appointmentService.getByElder(elderId));
  }

  @PostMapping("/{id}/complete")
  public ResponseEntity<?> complete(@PathVariable String id) {
    return appointmentService.markCompleted(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
