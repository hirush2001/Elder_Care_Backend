package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.*;
import com.eldercare.eldercare.repository.*;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final DoctorService doctorService;
  private final ElderRepository elderRepository;

  public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService, ElderRepository elderRepository) {
    this.appointmentRepository = appointmentRepository;
    this.doctorService = doctorService;
    this.elderRepository = elderRepository;
  }

  // Book appointment: check doctor's availability and conflicts
  public Appointment bookAppointment(String elderId, String doctorId, LocalDateTime requestedDateTime, String reason) {
    // 1) ensure elder & doctor exist
    Elder elder = elderRepository.findById(elderId).orElseThrow(() -> new IllegalArgumentException("Elder not found"));
    Doctor doctor = doctorService.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

    // 2) check availability: find doctor's availability for that day of week
    DayOfWeek day = requestedDateTime.toLocalDate().getDayOfWeek();
    List<DoctorAvailability> avails = doctorService.getAvailabilityForDay(doctorId, day);
    boolean withinAvailability = avails.stream().anyMatch(a ->
        !requestedDateTime.toLocalTime().isBefore(a.getStartTime()) &&
        !requestedDateTime.toLocalTime().isAfter(a.getEndTime().minusMinutes(1))
    );
    if (!withinAvailability) {
      throw new IllegalStateException("Doctor is not available at requested date/time");
    }

    // 3) check conflict: no other appointment at exact same time
    if (appointmentRepository.existsByDoctorIdAndAppointmentDateTime(doctorId, requestedDateTime)) {
      throw new IllegalStateException("Requested time already booked");
    }

    // 4) create appointment
    Appointment ap = Appointment.builder()
        .elder(elder)
        .doctor(doctor)
        .appointmentDateTime(requestedDateTime)
        .status(Appointment.Status.SCHEDULED)
        .reason(reason)
        .build();

    return appointmentRepository.save(ap);
  }

  public List<Appointment> getByElder(String elderId) { return appointmentRepository.findByElderId(elderId); }

  public Optional<Appointment> markCompleted(String appointmentId) {
    return appointmentRepository.findById(appointmentId).map(ap -> {
      ap.setStatus(Appointment.Status.COMPLETED);
      return appointmentRepository.save(ap);
    });
  }
}
