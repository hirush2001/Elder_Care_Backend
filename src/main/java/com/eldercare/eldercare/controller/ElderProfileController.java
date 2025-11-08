package com.eldercare.eldercare.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eldercare.eldercare.config.JwtUtil;
import com.eldercare.eldercare.model.ElderProfile;
import com.eldercare.eldercare.model.MedicationSchedule;
import com.eldercare.eldercare.service.ElderProfileService;
import com.eldercare.eldercare.model.Elder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.eclipse.angus.mail.handlers.message_rfc822;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/profile")
public class ElderProfileController {

    private final ElderProfileService elderProfileService;

    private JwtUtil jwtUtil;

    public ElderProfileController(ElderProfileService elderProfileService, JwtUtil jwtUtil) {
        this.elderProfileService = elderProfileService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/elderprofile")
    public String record(@RequestBody ElderProfile elderProfile, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("Missing or Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String elderId = jwtUtil.extractElderId(token);
        if (elderId == null) {
            throw new RuntimeException("Invalid token: elderId not found");
        }

        String newRegId = elderProfileService.generatedRegistrationId();
        elderProfile.setRegId(newRegId);

        Elder elder = elderProfileService.findElderById(elderId);
        elderProfile.setElder(elder);

        elderProfileService.addProfile(elderProfile);

        return "Profile added successfully for elder ID: " + elderId;
    }

    @GetMapping("/{RegId}")
    public ResponseEntity<?> getProfileById(@PathVariable String RegId) {
        try {
            ElderProfile reg = elderProfileService.getProfileById(RegId);
            return ResponseEntity.ok(reg);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profile not found with ID:" + RegId);
        }
    }

    @PutMapping("/{RegId}")
    public ResponseEntity<?> updateElderProfile(@PathVariable String RegId,
            @RequestBody ElderProfile updateElderProfile) {
        try {
            ElderProfile reg = elderProfileService.updateElderProfile(RegId, updateElderProfile);
            return ResponseEntity.ok("Profile updated successfully: " + reg);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profile not found with ID: " + RegId);
        }
    }

}
