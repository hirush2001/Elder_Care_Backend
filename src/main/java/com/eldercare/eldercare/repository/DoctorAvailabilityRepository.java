package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, String> {
  List<DoctorAvailability> findByDoctorId(String doctorId);
  List<DoctorAvailability> findByDoctorIdAndDayOfWeek(String doctorId, DayOfWeek day);
}
