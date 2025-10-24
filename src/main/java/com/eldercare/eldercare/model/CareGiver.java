package com.eldercare.eldercare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "caregiver")
public class CareGiver {

    @Id
    @Column(name = "CareTaker_Id", nullable = false, unique = true)
    private String careId;

    @Column(name = "Full_Name", nullable = false)
    private String fullname;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Contact_Number", nullable = false, length = 15)
    private String contactNumber;

    // ✅ Default constructor (required by JPA)
    public CareGiver() {
    }

    // ✅ Constructor with all fields
    public CareGiver(String careId, String fullname, String email, String contactNumber) {
        this.careId = careId;
        this.fullname = fullname;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    // ✅ Getters and Setters
    public String getCareId() {
        return careId;
    }

    public void setCareId(String careId) {
        this.careId = careId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
