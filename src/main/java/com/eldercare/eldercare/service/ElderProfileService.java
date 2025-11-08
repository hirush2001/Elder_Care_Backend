package com.eldercare.eldercare.service;

import java.util.Comparator;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.model.ElderProfile;
import com.eldercare.eldercare.repository.ElderProfileRepository;
import com.eldercare.eldercare.repository.ElderRepository;

@Service
public class ElderProfileService {

    public final ElderProfileRepository elderProfileRepository;
    public final ElderRepository elderRepository;

    public ElderProfileService(ElderProfileRepository elderProfileRepository, ElderRepository elderRepository) {
        this.elderProfileRepository = elderProfileRepository;
        this.elderRepository = elderRepository;
    }

    public ElderProfile addProfile(ElderProfile elderProfile) {
        return elderProfileRepository.save(elderProfile);
    }

    public Elder findElderById(String elderId) {
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new RuntimeException("Elder not found with ID: " + elderId));
    }

    public String generatedRegistrationId() {
        Optional<ElderProfile> last = elderProfileRepository.findAll()
                .stream()
                .filter(med -> med.getRegId() != null) // ignore null IDs
                .max(Comparator.comparing(ElderProfile::getRegId));

        if (last.isPresent()) {
            String lastId = last.get().getRegId();
            int num = Integer.parseInt(lastId.substring(1));
            num++;
            return String.format("P%03d", num);
        } else {
            return "P001"; // first ID if none exist
        }
    }
}
