package com.eldercare.eldercare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "elders")
public class Elder {

    @Id
    private String elderId;
    private String email;
    private String password;
    private String role;

    // Default constructor
    public Elder() {
    }

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
