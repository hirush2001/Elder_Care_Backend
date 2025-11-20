package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.Doctor;
import com.eldercare.eldercare.model.DoctorAvailability;
import com.eldercare.eldercare.repository.DoctorAvailabilityRepository;
import com.eldercare.eldercare.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
  private final DoctorRepository doctorRepository;
  private final DoctorAvailabilityRepository availabilityRepository;

  public DoctorService(DoctorRepository doctorRepository, DoctorAvailabilityRepository availabilityRepository) {
    this.doctorRepository = doctorRepository;
    this.availabilityRepository = availabilityRepository;
  }

  public Doctor save(Doctor d) { return doctorRepository.save(d); }
  public Optional<Doctor> findById(String id) { return doctorRepository.findById(id); }
  public List<DoctorAvailability> getAvailability(String doctorId) { return availabilityRepository.findByDoctorId(doctorId); }
  public List<DoctorAvailability> getAvailabilityForDay(String doctorId, DayOfWeek day) {
    return availabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, day);
  }
  public DoctorAvailability addAvailability(DoctorAvailability a) { return availabilityRepository.save(a); }
}
