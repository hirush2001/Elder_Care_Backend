/*
 * package com.eldercare.eldercare.controller;
 * 
 * import org.springframework.web.bind.annotation.*;
 * 
 * import com.eldercare.eldercare.model.DailyHealthRecord;
 * import com.eldercare.eldercare.service.*;
 * 
 * import org.springframework.web.bind.annotation.PostMapping;
 * import org.springframework.web.bind.annotation.RequestBody;
 * 
 * @RestController
 * 
 * @RequestMapping("/health")
 * public class DailyHealthREcordController {
 * 
 * private final HealthService healthService;
 * 
 * public DailyHealthREcordController(HealthService healthService) {
 * this.healthService = healthService;
 * }
 * 
 * @PostMapping("/record")
 * public DailyHealthRecord record(@RequestBody DailyHealthRecord healthrecord)
 * {
 * return healthService.create(healthrecord);
 * }
 * 
 * }
 * 
 * 
 */