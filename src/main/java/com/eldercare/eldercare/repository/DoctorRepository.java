package com.eldercare.eldercare.repository;

import com.eldercare.eldercare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
}
