package com.eldercare.eldercare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  @Id
   @Column(name = "Booking_id", nullable = false, unique = true)
   private String bookingId;
  @ManyToOne
  @JoinColumn(name = "Doctor_id", referencedColumnName = "Doctor_id")
  private Doctor doctor;

  // Day of week and a start/end time represent recurring weekly availability
  @Enumerated(EnumType.STRING)
  @Column(name = "Day_of_Week")
  private DayOfWeek dayOfWeek;

  @Column(name = "Start_Time")
  private LocalTime startTime;

  @Column(name = "End_Time")
  private LocalTime endTime;
}
