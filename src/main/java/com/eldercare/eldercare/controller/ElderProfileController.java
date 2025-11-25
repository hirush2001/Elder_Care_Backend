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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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

    @GetMapping("/guardian")
    public ResponseEntity<?> getProfileForGuardian(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or Invalid token");
            }
            String token = authHeader.substring(7);
            String elderId = jwtUtil.extractElderId(token);

            if (elderId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Elder ID not found in token");

            }

            ElderProfile elderProfile = elderProfileService.getProfileByElderId(elderId);
            return ResponseEntity.ok(elderProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching guardian's elder profile:" + e.getMessage());
        }
    }

    @GetMapping("/guardian/profile")
    public ResponseEntity<?> getGurdianProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            String elderId = jwtUtil.extractElderId(token);

            Optional<ElderProfile> elderProfile = elderProfileService.getGuardianProfileByTokenElderId(elderId);
            return elderProfile
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body("Profile not found with ID: " + elderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching Profile: " + e.getMessage());
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
