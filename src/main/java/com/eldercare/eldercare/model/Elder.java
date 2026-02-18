package com.eldercare.eldercare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@NoArgsConstructor
@Data
@Table(name = "elders")
public class Elder {

    @Id
    @Column(name = "Elder_id", nullable = false, unique = true)
    private String elderId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "Role")
    private String role;
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "full_name")
    private String fullName;

    /**
     * One Elder → Many Daily Health Records
     * 
     * mappedBy = "elder" → relation controlled by DailyHealthRecord entity
     * 
     */
    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DailyHealthRecord> healthRecords;

    // One Elder → One ElderProfile
    @OneToOne(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private ElderProfile elderProfile;

    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MedicationSchedule> medicationSchedules;

    /**
     * One Elder → Many Care Requests
     * JsonIgnoreProperties prevents infinite loop:
     * Elder → CareRequest → Elder → CareRequest → ...
     */
    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "elder" })
    private List<CareRequest> careRequests;

    // Parameterized constructor
    public Elder(String elderId, String email, String password, String role) {
        this.elderId = elderId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public String getElderId() {
        return elderId;
    }

    public void setElderId(String elderId) {
        this.elderId = elderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
