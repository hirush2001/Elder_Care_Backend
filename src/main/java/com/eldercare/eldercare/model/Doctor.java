package com.eldercare.eldercare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Doctotr")
public class Doctor {
        @Id
    @Column(name = "Doc_id", nullable = false, unique = true)
    private String regId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "Hospital")
    private String hospital;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;




}
