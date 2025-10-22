package com.eldercare.eldercare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity

@NoArgsConstructor
@Data
@Table(name = "medication_schedule")
public class MedicationSchedule {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "MedId")
    private String medId;
    @Column(name = "Medicine_Name")
    private String medicineName;
    @Column(name = "Dosage")
    private String dosage;
    @Column(name = "Time")
    private String time; // can be "HH:mm" OR full date string

    public MedicationSchedule(String medicineName, String dosage, String time) {

        this.medicineName = medicineName;
        this.dosage = dosage;
        this.time = time;

    }

    /**
     * Convert any stored time string to LocalTime.
     * Supports:
     * 1) "HH:mm" -> 11:45
     * 2) "EEE MMM dd HH:mm:ss z yyyy" -> Thu Oct 16 11:45:00 IST 2025
     */

    // Getters and setters
    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
