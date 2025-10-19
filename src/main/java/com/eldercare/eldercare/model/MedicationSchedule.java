package com.eldercare.eldercare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Document(collection = "medications")
public class MedicationSchedule {

    @Id

    private String medicineName;
    private String dosage;
    private String time; // can be "HH:mm" OR full date string

    public MedicationSchedule() {
    }

    public MedicationSchedule(String medicineName, String dosage, String time, String elderEmail) {
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
