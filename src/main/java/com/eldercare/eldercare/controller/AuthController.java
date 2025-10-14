package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ElderRepository elderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Elder elder) {
        try {
            Optional<Elder> existing = elderRepository.findByEmail(elder.getEmail());
            if (existing.isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Email already in use"));
            }

            elder.setPassword(passwordEncoder.encode(elder.getPassword()));
            if (elder.getRole() == null || elder.getRole().isEmpty()) {
                elder.setRole("ROLE_OWNER");

            }

            Elder saved = elderRepository.save(elder);
            return ResponseEntity.ok(Map.of(
                    "id", saved.getId(),
                    "email", saved.getEmail(),
                    "password", saved.getPassword(),
                    "role", saved.getRole()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Signup failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Elder loginData) {
        try {
            System.out.println("Login attempt for: " + loginData.getEmail());
            Optional<Elder> userOpt = elderRepository.findByEmail(loginData.getEmail());

            if (userOpt.isEmpty()) {
                System.out.println("No user found");
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }

            Elder user = userOpt.get();
            System.out.println("User password from DB: " + user.getPassword());
            boolean matches = passwordEncoder.matches(loginData.getPassword(), user.getPassword());
            System.out.println("Password match: " + matches);

            if (!matches) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            System.out.println("Token generated successfully: " + token);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", Map.of(
                            "email", user.getEmail(),
                            "password", user.getPassword(),
                            "role", user.getRole())));

        } catch (Exception e) {
            e.printStackTrace(); // 🔹 important: show the real exception
            return ResponseEntity.internalServerError().body(Map.of("error", "Login failed"));
        }
    }

    @GetMapping("/api/test")
    public ResponseEntity<?> test(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return ResponseEntity.ok(Map.of("message", "Token received!", "token", token));
        }
        return ResponseEntity.ok(Map.of("message", "No token provided"));
    }

}
