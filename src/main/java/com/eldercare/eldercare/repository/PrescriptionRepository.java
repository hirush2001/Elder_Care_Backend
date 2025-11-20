package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, String> {
  List<Prescription> findByElderId(String elderId);
}
