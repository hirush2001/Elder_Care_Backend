package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ElderRepository elderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Elder signup(Elder elder) {
        elder.setPassword(passwordEncoder.encode(elder.getPassword()));
        return elderRepository.save(elder);
    }

    public boolean verifyCredentials(String email, String rawPassword) {
        Optional<Elder> userOpt = elderRepository.findByEmail(email);
        if (userOpt.isEmpty())
            return false;

        Elder elder = userOpt.get();
        return passwordEncoder.matches(rawPassword, elder.getPassword());
    }
}
