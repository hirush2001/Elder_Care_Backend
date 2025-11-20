package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.*;
import com.eldercare.eldercare.repository.PrescriptionRepository;

import lombok.Data;

import com.eldercare.eldercare.repository.DoctorRepository;
import com.eldercare.eldercare.repository.ElderRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class PrescriptionService {
  private final PrescriptionRepository prescriptionRepository;
  private final DoctorRepository doctorRepository;
  private final ElderRepository elderRepository;

  public PrescriptionService(PrescriptionRepository prescriptionRepository, DoctorRepository doctorRepository, ElderRepository elderRepository) {
    this.prescriptionRepository = prescriptionRepository;
    this.doctorRepository = doctorRepository;
    this.elderRepository = elderRepository;
  }

  // Create and save prescription (medications are cascaded)
  public Prescription addPrescription(String doctorId, String elderId, String notes, List<MedicationSchedule> meds) {
    Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    Elder elder = elderRepository.findById(elderId).orElseThrow(() -> new IllegalArgumentException("Elder not found"));
    
   Prescription p = Prescription.builder()
    .doctor(doctor)
    .elder(elder)
    .prescribedDate(LocalDate.now())
    .notes(notes)
    .medicationSchedule(meds)
    .build();

meds.forEach(m -> m.setPrescription(p));

 prescriptionRepository.save(p);


    return prescriptionRepository.save(p);
  }

  public List<Prescription> getForElder(String elderId) {
    return prescriptionRepository.findByElderId(elderId);
  }
}
