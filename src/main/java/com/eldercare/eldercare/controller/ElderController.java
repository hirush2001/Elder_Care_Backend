/*
 * package com.eldercare.eldercare.controller;
 * 
 * import org.springframework.web.bind.annotation.*;
 * 
 * import com.eldercare.eldercare.config.JwtUtil;
 * import com.eldercare.eldercare.model.Elder;
 * import com.eldercare.eldercare.service.ElderService;
 * 
 * import java.util.List;
 * 
 * @RestController
 * 
 * @RequestMapping("/elders")
 * public class ElderController {
 * 
 * private final ElderService elderService;
 * private final JwtUtil jwtUtil;
 * 
 * public ElderController(ElderService elderService, JwtUtil jwtUtil) {
 * this.elderService = elderService;
 * this.jwtUtil = jwtUtil;
 * }
 * 
 * // Register elder
 * 
 * @PostMapping("/register")
 * public Elder register(@RequestBody Elder elder) {
 * return elderService.create(elder);
 * }
 * 
 * // Get all elders
 * 
 * @GetMapping
 * public List<Elder> getAll() {
 * return elderService.getAll();
 * }
 * 
 * // Login (check password)
 * 
 * @PostMapping("/login")
 * public String login(@RequestBody Elder elder) {
 * Elder found = elderService.getAll().stream()
 * .filter(e -> e.getName().equals(elder.getName()))
 * .findFirst().orElse(null);
 * 
 * if (found != null && elderService.checkPassword(elder.getPassword(),
 * found.getPassword())) {
 * String token = jwtUtil.generateToken(found.getName());
 * return "Bearer " + token; // Return token
 * } else {
 * return "Invalid credentials";
 * }
 * }
 * 
 * }
 * 
 */