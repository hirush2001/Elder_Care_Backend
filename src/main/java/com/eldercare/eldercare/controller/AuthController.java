package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.config.JwtUtil;
import com.eldercare.eldercare.service.UserService;
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

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Elder elder) {
        try {
            Optional<Elder> existing = elderRepository.findByEmail(elder.getEmail());
            if (existing.isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Email already in use"));
            }
            elder.setElderId(userService.generateElderId());

            elder.setPassword(passwordEncoder.encode(elder.getPassword()));
            if (elder.getRole() == null || elder.getRole().isEmpty()) {
                elder.setRole("ROLE_OWNER");

            }

            Elder saved = elderRepository.save(elder);
            return ResponseEntity.ok(Map.of(
                    "elderId", saved.getElderId(),
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
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
            }

            Elder user = userOpt.get();
            System.out.println("User password from DB: " + user.getPassword());
            boolean matches = passwordEncoder.matches(loginData.getPassword(), user.getPassword());
            System.out.println("Password match: " + matches);

            // 🔹 Check password validity
            if (!matches) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
            }

            // 🔹 Check role validity
            if (loginData.getRole() == null || !loginData.getRole().equalsIgnoreCase(user.getRole())) {
                System.out.println("Role mismatch: expected " + user.getRole() + " but got " + loginData.getRole());
                return ResponseEntity.status(403).body(Map.of("error", "Invalid role for this account"));
            }

            // 🔹 Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getElderId());
            System.out.println("Token generated successfully: " + token);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", Map.of(
                            "email", user.getEmail(),
                            "role", user.getRole(),
                            "elderId", user.getElderId())));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Login failed"));
        }
    }

    // GET Elder by ID
    @GetMapping("/elder/{id}")
    public ResponseEntity<?> getElderById(@PathVariable String id) {
        try {
            Optional<Elder> userOpt = elderRepository.findById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Elder not found"));
            }
            Elder user = userOpt.get();
            return ResponseEntity.ok(Map.of(
                    "elderId", user.getElderId(),
                    "email", user.getEmail(),
                    "role", user.getRole()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch elder"));
        }
    }

    // DELETE Elder by ID
    @DeleteMapping("/elder/{id}")
    public ResponseEntity<?> deleteElderById(@PathVariable String id) {
        try {
            Optional<Elder> userOpt = elderRepository.findById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Elder not found"));
            }

            elderRepository.deleteById(id); // ✅ simpler and safer
            return ResponseEntity.ok(Map.of("message", "Elder deleted successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete elder"));
        }
    }

    // UPDATE Elder by ID
    @PutMapping("/elder/{id}")
    public ResponseEntity<?> updateElderById(@PathVariable String id, @RequestBody Elder updateData) {
        try {
            Optional<Elder> userOpt = elderRepository.findById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Elder not found"));
            }

            Elder user = userOpt.get();
            if (updateData.getEmail() != null)
                user.setEmail(updateData.getEmail());
            if (updateData.getPassword() != null)
                user.setPassword(passwordEncoder.encode(updateData.getPassword()));
            if (updateData.getRole() != null)
                user.setRole(updateData.getRole());

            Elder saved = elderRepository.save(user);
            return ResponseEntity.ok(Map.of(
                    "elderId", saved.getElderId(),
                    "email", saved.getEmail(),
                    "role", saved.getRole()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to update elder"));
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
