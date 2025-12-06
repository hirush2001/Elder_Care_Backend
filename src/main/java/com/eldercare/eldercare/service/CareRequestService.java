package com.eldercare.eldercare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eldercare.eldercare.model.CareGiver;
import com.eldercare.eldercare.model.CareRequest;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.repository.ElderProfileRepository;
import com.eldercare.eldercare.repository.CareRequestRepository;
import com.eldercare.eldercare.repository.CareTakerRepository;

@Service
public class CareRequestService {
    private final CareRequestRepository careRequestRepository;
    private final CareTakerRepository careTakerRepository;
    private final EmailService emailService;

    public CareRequestService(CareRequestRepository careRequestRepository,
            CareTakerRepository careTakerRepository, EmailService emailService) {
        this.careRequestRepository = careRequestRepository;
        this.careTakerRepository = careTakerRepository;
        this.emailService = emailService;
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

    public String requestEmail(CareRequest careRequest, Elder elder) {
        String status = careRequest.getStatus();
        StringBuilder message = new StringBuilder();
        String subject = "";

        if (status == null) {
            return "Invalid status: null";
        }

        String statusLower = status.toLowerCase();

        if (statusLower.equals("accepted")) {

            subject = "Care Request Accepted ✔";

            message.append("Dear ")
                    .append(elder.getFullName())
                    .append(",\n\n")
                    .append("Good news! Your care request (Request ID: ")
                    .append(careRequest.getRequestId())
                    .append(") has been *accepted* by the caregiver.\n")
                    .append("The caregiver will contact you soon.\n\n")
                    .append("Thank you,\nElderCare Support Team");

            emailService.sendEmail(elder.getEmail(), subject, message.toString());

            return "Accepted email sent.";

        }

        if (statusLower.equals("rejected")) {

            subject = "Care Request Rejected ❗";

            message.append("Dear ")
                    .append(elder.getFullName())
                    .append(",\n\n")
                    .append("We regret to inform you that your care request (Request ID: ")
                    .append(careRequest.getRequestId())
                    .append(") has been *rejected* by the caregiver.\n")
                    .append("You may try sending a request to another caregiver.\n\n")
                    .append("Thank you,\nElderCare Support Team");

            emailService.sendEmail(elder.getEmail(), subject, message.toString());

            return "Rejected email sent.";

        }

        if (statusLower.equals("pending")) {
            return "Request is still pending — no email sent.";
        }

        return "Unknown status: " + status;
    }

    public String sendRequestEmail(CareRequest careRequest, Elder elder, Elder caregiver) {

        StringBuilder message = new StringBuilder();
        String status = careRequest.getStatus().toLowerCase();
        String subject;

        if (status.equals("pending")) {

            subject = "New Care Request Pending ⏳";

            message.append("Hello ")
                    .append(caregiver.getFullName()).append(",\n\n")
                    .append("You have received a new care request from Elder:\n")
                    .append("Elder ID: ").append(elder.getElderId()).append("\n")
                    .append("Elder Name: ").append(elder.getFullName()).append("\n\n")
                    .append("Request ID: ").append(careRequest.getRequestId()).append("\n")
                    .append("Please log in to your dashboard to review the request.\n\n")
                    .append("Thank you,\nElderCare Support Team");

            // ✔️ send email to caregiver, not elder
            emailService.sendEmail(caregiver.getEmail(), subject, message.toString());

            return "Pending email sent to caregiver.";
        }

        return "No email sent.";
    }

}
