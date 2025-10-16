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
        String email = auth.getName(); // This is the email used to login

        // Lookup Elder by email
        Elder elder = healthService.findElderByEmail(email);

        // Save record
        DailyHealthRecord savedRecord = healthService.create(healthrecord);

        // Generate message & send email if needed
        String message = healthService.healthsummary(healthrecord, elder);

        Map<String, Object> response = new HashMap<>();
        response.put("record", savedRecord);
        response.put("message", message);

        return response;

    }
}
