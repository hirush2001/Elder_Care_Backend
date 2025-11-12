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

    public ElderProfile findProfileByElderId(String elderId) {
        return elderProfileRepository.findByElder_ElderId(elderId)
                .orElse(null); // or throw exception
    }

    public ElderProfile getProfileById(String elderId) {
        return elderProfileRepository.findById(
                elderId)
                .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + elderId));
    }

    public ElderProfile getProfileByElderId(String elderId) {
        return elderProfileRepository.findByElder_ElderId(elderId)
                .orElseThrow(() -> new RuntimeException("Profile not found for elder ID: " + elderId));
    }

    public ElderProfile updateElderProfile(String regId, ElderProfile updatedProfile) {
        return elderProfileRepository.findById(regId)
                .map(profile -> {
                    profile.setFullName(updatedProfile.getFullName());
                    profile.setAge(updatedProfile.getAge());
                    profile.setGender(updatedProfile.getGender());
                    profile.setEmail(updatedProfile.getEmail());
                    profile.setPhone(updatedProfile.getPhone());
                    profile.setAddress(updatedProfile.getAddress());

                    profile.setGuardianFullName(updatedProfile.getGuardianFullName());
                    profile.setGuardianRelationship(updatedProfile.getGuardianRelationship());
                    profile.setGuardianEmail(updatedProfile.getGuardianEmail());
                    profile.setGuardianPhone(updatedProfile.getGuardianPhone());

                    return elderProfileRepository.save(profile);
                })
                .orElseThrow(() -> new RuntimeException("Elder profile not found with ID: " + regId));
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
