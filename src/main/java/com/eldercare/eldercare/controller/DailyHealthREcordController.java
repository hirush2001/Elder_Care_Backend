package com.eldercare.eldercare.controller;

import java.util.HashMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.config.JwtUtil;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.service.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController

@RequestMapping("/health")
public class DailyHealthREcordController {

    private final HealthService healthService;

    private JwtUtil jwtUtil;

    public DailyHealthREcordController(HealthService healthService, JwtUtil jwtUtil) {
        this.healthService = healthService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/record")

    public Map<String, Object> record(@RequestBody DailyHealthRecord healthrecord, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or Invalid Authorization header");

        }

        String token = authHeader.substring(7);
        String role = jwtUtil.extractRole(token);

        if (role == null || !role.equalsIgnoreCase("Elder")) {
            throw new RuntimeException("Access denied: Only Elders can record health data");
        }

        // Get logged-in elder's email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Lookup Elder by email
        Elder elder = healthService.findElderByEmail(email);

        // Add auto-generated healthId BEFORE saving
        String newHealthId = healthService.generateHealthId();
        healthrecord.setHealthId(newHealthId);

        // Generate message & send email if needed
        String message = healthService.healthsummary(healthrecord, elder);

        // Link the elder object
        healthrecord.setElder(elder);

        // Now save the record
        DailyHealthRecord savedRecord = healthService.create(healthrecord);

        Map<String, Object> response = new HashMap<>();
        response.put("record", savedRecord);
        response.put("message", message);

        return response;
    }

    // GET a single record by healthId
    @GetMapping("/record/{healthId}")
    public Map<String, Object> getRecord(
            @PathVariable String healthId,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        // 1. Get Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or Invalid Authorization header");
        }

        // 2. Extract token
        String token = authHeader.substring(7);

        // 3. Extract role from token
        String role = jwtUtil.extractRole(token);
        if (role == null || !role.equalsIgnoreCase("Elder")) {
            throw new RuntimeException("Access denied: Only Elders can access health records");
        }

        // 4. Get logged-in elder's email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // 5. Lookup Elder by email (optional, for logging or validation)
        Elder elder = healthService.findElderByEmail(email);

        try {
            // 6. Fetch the record
            DailyHealthRecord record = healthService.findById(healthId);

            // Optional: validate that this record belongs to the elder
            if (!record.getElder().getEmail().equals(email)) {
                throw new RuntimeException("Access denied: Record does not belong to this Elder");
            }

            response.put("record", record);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
        }

        return response;
    }

    // UPDATE a record by healthId
    @PutMapping("/record/{healthId}")
    public Map<String, Object> updateRecord(
            @PathVariable String healthId,
            @RequestBody DailyHealthRecord updatedRecord,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        // 1. Get Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or Invalid Authorization header");
        }

        // 2. Extract token
        String token = authHeader.substring(7);

        // 3. Extract role
        String role = jwtUtil.extractRole(token);
        if (role == null || !role.equalsIgnoreCase("Elder")) {
            throw new RuntimeException("Access denied: Only Elders can update health records");
        }

        // 4. Get logged-in elder's email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        try {
            // Fetch existing record
            DailyHealthRecord existingRecord = healthService.findById(healthId);

            // Ensure record belongs to the logged-in Elder
            if (!existingRecord.getElder().getEmail().equals(email)) {
                throw new RuntimeException("Access denied: Record does not belong to this Elder");
            }

            // Update the record
            DailyHealthRecord record = healthService.updateHealthRecord(healthId, updatedRecord);
            response.put("record", record);
            response.put("message", "Health record updated successfully");

        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
        }

        return response;
    }

    // DELETE a record by healthId
    @DeleteMapping("/record/{healthId}")
    public Map<String, Object> deleteRecord(
            @PathVariable String healthId,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        // 1. Get Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or Invalid Authorization header");
        }

        // 2. Extract token
        String token = authHeader.substring(7);

        // 3. Extract role
        String role = jwtUtil.extractRole(token);
        if (role == null || !role.equalsIgnoreCase("Elder")) {
            throw new RuntimeException("Access denied: Only Elders can delete health records");
        }

        // 4. Get logged-in elder's email
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        try {
            DailyHealthRecord record = healthService.findById(healthId);

            // Ensure record belongs to the logged-in Elder
            if (!record.getElder().getEmail().equals(email)) {
                throw new RuntimeException("Access denied: Record does not belong to this Elder");
            }

            healthService.deleteById(healthId);
            response.put("message", "Health record with ID " + healthId + " deleted successfully");

        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/records")
    public Map<String, Object> getAllRecords(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Check role
        String role = jwtUtil.extractRole(token);
        if (role == null || !role.equalsIgnoreCase("Elder")) {
            throw new RuntimeException("Access denied: Only Elders can access health records");
        }

        // Extract elder_id from token
        String elderId = jwtUtil.extractElderId(token);

        // Fetch all health records for this elder
        List<DailyHealthRecord> records = healthService.findAllByElderId(elderId);

        response.put("records", records);
        response.put("count", records.size());

        return response;
    }

}
