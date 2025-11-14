package com.eldercare.eldercare.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Prescription")
public class Prescription {
    @Id
    @Column(name = "Prescription_Id", nullable = false, unique = true)
    private String PrescriptionId;
    
    // 🔹 Many Prescriptions belong to one Elder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Elder_Id", referencedColumnName = "Elder_Id")
    @JsonIgnore
    private Elder elder;

    
    // 🔹 Many Prescriptions can be done one doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Doc_id", referencedColumnName = "Doc_id")
    @JsonIgnore
    private CareGiver careGiver;

    @Column(name = "Date_and_Time", nullable = false)
    private LocalDateTime PrescriptionDateTime;

     private LocalDate prescribedDate;

     @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<MedicationSchedule> medications = new ArrayList<>();

    @Column(name = "Notes", nullable = false, length = 2000)
    private String notes;

   

}
