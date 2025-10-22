package com.eldercare.eldercare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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

    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DailyHealthRecord> healthRecords;

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
