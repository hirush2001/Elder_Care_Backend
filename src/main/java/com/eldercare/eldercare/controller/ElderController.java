package com.eldercare.eldercare.controller;

import org.springframework.web.bind.annotation.*;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.service.ElderService;

import java.util.List;

@RestController
@RequestMapping("/elders")
public class ElderController {

    private final ElderService elderService;

    public ElderController(ElderService elderService) {
        this.elderService = elderService;
    }

    // Register elder
    @PostMapping("/register")
    public Elder register(@RequestBody Elder elder) {
        return elderService.create(elder);
    }

    // Get all elders
    @GetMapping
    public List<Elder> getAll() {
        return elderService.getAll();
    }

    // Login (check password)
    @PostMapping("/login")
    public String login(@RequestBody Elder elder) {
        Elder found = elderService.getAll().stream()
                .filter(e -> e.getName().equals(elder.getName()))
                .findFirst().orElse(null);

        if (found != null && elderService.checkPassword(elder.getPassword(), found.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid credentials";
        }
    }
}
