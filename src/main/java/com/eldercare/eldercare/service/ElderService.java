/*
 * package com.eldercare.eldercare.service;
 * 
 * import org.springframework.stereotype.Service;
 * import org.springframework.security.crypto.password.PasswordEncoder;
 * import com.eldercare.eldercare.model.Elder;
 * import com.eldercare.eldercare.repository.ElderRepository;
 * 
 * import java.util.List;
 * 
 * @Service
 * public class ElderService {
 * 
 * private final ElderRepository repo;
 * private final PasswordEncoder passwordEncoder;
 * 
 * public ElderService(ElderRepository repo, PasswordEncoder passwordEncoder) {
 * this.repo = repo;
 * this.passwordEncoder = passwordEncoder;
 * }
 * 
 * // Create elder (with password hashing)
 * public Elder create(Elder e) {
 * e.setPassword(passwordEncoder.encode(e.getPassword())); // hash password
 * return repo.save(e);
 * }
 * 
 * public List<Elder> getAll() {
 * return repo.findAll();
 * }
 * 
 * public Elder get(String id) {
 * return repo.findById(id).orElse(null);
 * }
 * 
 * public void delete(String id) {
 * repo.deleteById(id);
 * }
 * 
 * // Check password during login
 * public boolean checkPassword(String rawPassword, String storedHash) {
 * return passwordEncoder.matches(rawPassword, storedHash);
 * }
 * }
 * 
 */