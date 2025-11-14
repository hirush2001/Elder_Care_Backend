package com.eldercare.eldercare.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Appointment")
public class Appointment {
    @Id
    @Column(name = "Appointment_Id", nullable = false, unique = true)
    private String AppointmentId;
    
    // 🔹 Many appointments belong to one Elder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Elder_Id", referencedColumnName = "Elder_Id")
    @JsonIgnore
    private Elder elder;

    
    // 🔹 Many appointments can be assigned to one doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Doc_id", referencedColumnName = "Doc_id")
    @JsonIgnore
    private CareGiver careGiver;

    @Column(name = "Date_and_Time", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(name = "Reason", nullable = false, length = 2000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

     public enum Status {
      SCHEDULED,
      COMPLETED,
    CANCELLED
   }


}
