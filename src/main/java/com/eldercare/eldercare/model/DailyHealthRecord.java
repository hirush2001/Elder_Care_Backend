package com.eldercare.eldercare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@NoArgsConstructor
@Data
@Table(name = "health")
public class DailyHealthRecord {

    @Id
    @Column(name = "Health_Id", nullable = false, unique = true)
    private String healthId;
    @Column(name = "BPressure")
    private float bloodPressure;
    @Column(name = "Sugarlevel")
    private float sugarLevel;
    @Column(name = "Pulserate")
    private float pulseRate;
    @Column(name = "Temparature")
    private float temperature;

    @ManyToOne
    @JoinColumn(name = "elder_Id", referencedColumnName = "elder_id")
    @JsonBackReference
    private Elder elder; // assuming your Elder class is named Elder

    public DailyHealthRecord(String healthId, float bloodPressure, float sugarLevel, float pulseRate,
            float temperature) {
        this.healthId = healthId;
        this.bloodPressure = bloodPressure;
        this.sugarLevel = sugarLevel;
        this.pulseRate = pulseRate;
        this.temperature = temperature;
    }

    public Elder getElder() {
        return elder;
    }

    public void setElder(Elder elder) {
        this.elder = elder;
    }

    public String getHealthId() {
        return healthId;
    }

    public float getPressure() {
        return bloodPressure;
    }

    public float getSugar() {
        return sugarLevel;
    }

    public float getPulse() {
        return pulseRate;
    }

    public float getTemp() {
        return temperature;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public void setPressure(float bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public void setSugar(float sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public void setPulse(float pulseRate) {
        this.pulseRate = pulseRate;
    }

    public void setTemp(float temperature) {
        this.temperature = temperature;
    }
}
