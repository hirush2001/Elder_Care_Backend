package com.eldercare.eldercare.service;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderRepository;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Elder saveRequest(Elder elder) {
        return repo.save(elder);
    }

    public Elder findById(String elderId) {
        return repo.findById(elderId).orElse(null);
    }

    public String generateElderId() {
        List<Elder> elders = repo.findAll();

        if (elders.isEmpty()) {
            return "E001";
        }

        // Get the last Elder’s ID
        Elder lastElder = elders.get(elders.size() - 1);
        String lastId = lastElder.getElderId();

        int num = Integer.parseInt(lastId.substring(1)); // remove 'E'
        num++;
        return String.format("E%03d", num); // E001, E002, ...
    }

}
