package com.eldercare.eldercare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "DoctorAvailability")
public class DoctorAvailability {
       @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  // Day of week and a start/end time represent recurring weekly availability
  @Enumerated(EnumType.STRING)
  private DayOfWeek dayOfWeek;

  private LocalTime startTime;
  private LocalTime endTime;
}
