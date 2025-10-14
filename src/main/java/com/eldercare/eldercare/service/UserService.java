package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final ElderRepository repo;
    private final PasswordEncoder encoder;

    public UserService(ElderRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Elder register(Elder user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public Elder findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    public Elder save(Elder u) {
        return repo.save(u);
    }
}
