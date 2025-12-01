package com.eldercare.eldercare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eldercare.eldercare.model.CareGiver;
import com.eldercare.eldercare.model.CareRequest;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.repository.CareRequestRepository;
import com.eldercare.eldercare.repository.CareTakerRepository;

@Service
public class CareRequestService {
    private final CareRequestRepository careRequestRepository;
    private final CareTakerRepository careTakerRepository;

    public CareRequestService(CareRequestRepository careRequestRepository,
            CareTakerRepository careTakerRepository) {
        this.careRequestRepository = careRequestRepository;
        this.careTakerRepository = careTakerRepository;
    }

    public CareRequest saveRequest(CareRequest careRequest) {
        return careRequestRepository.save(careRequest);
    }

    public String generateCareReqId() {
        List<CareRequest> records = careRequestRepository.findAll();

        if (records.isEmpty()) {
            return "R001";
        }

        CareRequest lastRecord = records.get(records.size() - 1);
        String lastId = lastRecord.getRequestId();
        int num = Integer.parseInt(lastId.substring(1)); // remove 'C'
        num++;
        return String.format("R%03d", num);
    }

    public CareGiver findCareGiverById(String careTakerId) {
        return careTakerRepository.findById(careTakerId).orElse(null);
    }

    // ✅ Get all care requests
    public List<CareRequest> getAllRequests() {
        return careRequestRepository.findAll();
    }

    // ✅ Get single request
    public Optional<CareRequest> getRequestIdById(String requestId) {
        return careRequestRepository.findById(requestId);
    }

    // ✅ Delete care request
    public void deleteRequest(String requestId) {
        if (!careRequestRepository.existsById(requestId)) {
            throw new RuntimeException("Care request not found with ID: " + requestId);
        }
        careRequestRepository.deleteById(requestId);
    }

    public CareRequest updateRequest(String id, CareRequest updatedRequest) {
        return careRequestRepository.findById(id).map(existing -> {
            existing.setElder(updatedRequest.getElder());
            // existing.setCareGiver(updatedRequest.getCareGiver());
            existing.setRequestDate(updatedRequest.getRequestDate());
            existing.setStatus(updatedRequest.getStatus());
            // Add any other fields you want to update
            return careRequestRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Care request not found with ID: " + id));
    }

    public List<CareRequest> getRequestsForCaregiver(String caregiverId) {
        return careRequestRepository.findByCareIdAndFetchElder(caregiverId);
    }

    public List<CareRequest> findAllByElderId(String elderId) {
        return careRequestRepository.findAllByElder_ElderId(elderId);
    }

}
