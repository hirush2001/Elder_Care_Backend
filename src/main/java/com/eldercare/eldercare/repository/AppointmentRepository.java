package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
  List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(String doctorId, LocalDateTime start, LocalDateTime end);
  boolean existsByDoctorIdAndAppointmentDateTime(String doctorId, LocalDateTime dateTime);
  List<Appointment> findByElderId(String elderId);
}
