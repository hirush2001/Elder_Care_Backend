package com.eldercare.eldercare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eldercare.eldercare.model.CareGiver;

public interface CareTakerRepository extends JpaRepository<CareGiver, String> {

}
