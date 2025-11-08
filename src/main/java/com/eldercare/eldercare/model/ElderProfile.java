package com.eldercare.eldercare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "elder_profile")
public class ElderProfile {

    @Id
    @Column(name = "Reg_id", nullable = false, unique = true)
    private String regId;

    @Column(name = "Fullname")
    private String fullName;

    @Column(name = "Age")
    private String age;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Address")
    private String address;

    // Guardian Details
    @Column(name = "Guardian_Fullname")
    private String guardianFullName;

    @Column(name = "Guardian_Relationship")
    private String guardianRelationship;

    @Column(name = "Guardian_Email")
    private String guardianEmail;

    @Column(name = "Guardian_Phone")
    private String guardianPhone;

    // Getters and Setters
    @ManyToOne
    @JoinColumn(name = "elder_Id", referencedColumnName = "elder_id")
    @JsonBackReference
    private Elder elder;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGuardianFullName() {
        return guardianFullName;
    }

    public void setGuardianFullName(String guardianFullName) {
        this.guardianFullName = guardianFullName;
    }

    public String getGuardianRelationship() {
        return guardianRelationship;
    }

    public void setGuardianRelationship(String guardianRelationship) {
        this.guardianRelationship = guardianRelationship;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
}
