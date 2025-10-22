package com.eldercare.eldercare.controller;

import java.util.HashMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.service.*;
import java.util.Map;
import java.util.HashMap;

@RestController

@RequestMapping("/health")
public class DailyHealthREcordController {

    private final HealthService healthService;

    public DailyHealthREcordController(HealthService healthService) {
        this.healthService = healthService;
    }

    @PostMapping("/record")
    @PreAuthorize("hasRole('Elder')")
    public Map<String, Object> record(@RequestBody DailyHealthRecord healthrecord) {

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
    @PreAuthorize("hasRole('Elder')")
    public Map<String, Object> getRecord(@PathVariable String healthId) {
        Map<String, Object> response = new HashMap<>();
        try {
            DailyHealthRecord record = healthService.findById(healthId);
            response.put("record", record);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
        }
        return response;
    }

    // UPDATE a record by healthId
    @PutMapping("/record/{healthId}")
    @PreAuthorize("hasRole('Elder')")
    public Map<String, Object> updateRecord(@PathVariable String healthId,
            @RequestBody DailyHealthRecord updatedRecord) {
        Map<String, Object> response = new HashMap<>();
        try {
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
    @PreAuthorize("hasRole('Elder')")
    public Map<String, Object> deleteRecord(@PathVariable String healthId) {
        Map<String, Object> response = new HashMap<>();

        if (!healthService.existsById(healthId)) {
            response.put("message", "Health record with ID " + healthId + " not found");
            return response;
        }

        healthService.deleteById(healthId);
        response.put("message", "Health record with ID " + healthId + " deleted successfully");
        return response;
    }
}
