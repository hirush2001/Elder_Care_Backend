package com.eldercare.eldercare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "medication_schedule")
public class MedicationSchedule {

    @Id
    @Column(name = "MedId", nullable = false, unique = true)
    private String medId;
    @Column(name = "Medicine_Name")
    private String medicineName;
    @Column(name = "Dosage")
    private String dosage;
    @Column(name = "Time")
    private String time; // can be "HH:mm" OR full date string
    @Column(name = "Start_Date")
    private LocalDate startDate;
    @Column(name = "End_Date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "Elder_Id", referencedColumnName = "Elder_id")
    @JsonBackReference
    private Elder elder;
    @ManyToOne
    @JoinColumn(name = "Prescription_id")
    private Prescription prescription;
    

 }
