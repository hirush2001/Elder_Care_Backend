package com.eldercare.eldercare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eldercare.eldercare.config.JwtUtil;
import com.eldercare.eldercare.model.CareGiver;
import com.eldercare.eldercare.model.CareRequest;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.service.CareRequestService;
import com.eldercare.eldercare.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import com.eldercare.eldercare.model.Elder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/caretaker")
public class CareTakerRequestController {

    private final CareRequestService careRequestService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public CareTakerRequestController(CareRequestService careRequestService, UserService userService,
            JwtUtil jwtUtil) {
        this.careRequestService = careRequestService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;

    }

    // Create a new care request from an Elder to a Caregiver
    @PostMapping("/request/{req_id}")
    public ResponseEntity<?> addCareReq(
            @RequestBody CareRequest careRequest,
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("req_id") String req_id) {

        try {
            // Extract JWT token (remove "Bearer ")
            String token = authHeader.substring(7);

            // Identify elder sending request
            String elderId = jwtUtil.extractElderId(token);
            Elder elder = userService.findById(elderId);

            // Security validation
            if (!elder.getRole().equalsIgnoreCase("elder")) {
                return ResponseEntity.badRequest().body("Invalid elder token.");
            }

            // Caregiver ID coming from URI
            Elder careGiver = userService.findById(req_id);

            if (careGiver == null || !careGiver.getRole().equals("caregiver")) {
                return ResponseEntity.badRequest().body("Invalid caregiver ID.");
            }

            String newCareReqId = careRequestService.generateCareReqId();

            careRequest.setRequestId(newCareReqId);
            careRequest.setElder(elder); // Elder sending the request
            // careRequest.setCareGiver(careGiver); // Caregiver from URI
            careRequest.setCId(req_id);

            if (careRequest.getRequestDate() == null || careRequest.getRequestDate().isEmpty()) {
                careRequest.setRequestDate(java.time.LocalDate.now().toString());
            }

            careRequest.setStatus("Pending");

            CareRequest saved = careRequestService.saveRequest(careRequest);

            careRequestService.sendRequestEmail(saved, elder, careGiver);

            return ResponseEntity.ok("Successfully added Care Request with ID: " + saved.getRequestId());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding Request: " + e.getMessage());
        }
    }

    // ✅ GET — Get all care requests
    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests() {
        try {
            List<CareRequest> requests = careRequestService.getAllRequests();
            if (requests.isEmpty()) {
                return ResponseEntity.ok(" No care request found.");
            }
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch care requests: " + e.getMessage());
        }
    }

    // ✅ GET — Get care request by ID
    @GetMapping("/request/{care_taker_id}")
    public ResponseEntity<?> getCaretakerById(@PathVariable String care_taker_id) {
        try {
            Optional<CareRequest> careRequest = careRequestService.getRequestIdById(care_taker_id);
            return careRequest
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(
                            () -> ResponseEntity.status(404).body(" Request not found with ID: " + care_taker_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error fetching request: " + e.getMessage());
        }
    }

    @DeleteMapping("/request/{request_id}")
    public ResponseEntity<?> deleteRequest(@PathVariable String request_id) {
        try {
            careRequestService.deleteRequest(request_id);
            return ResponseEntity.ok("✅ Successfully deleted request with ID: " + request_id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("⚠️ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error deleting request: " + e.getMessage());
        }
    }

    // ✅ Update a care request
    @PutMapping("/request/{id}") // remove extra /update in URL
    public ResponseEntity<?> updateRequest(
            @PathVariable String id,
            @RequestBody CareRequest updatedRequest) {
        try {
            CareRequest updated = careRequestService.updateRequest(id, updatedRequest);
            return ResponseEntity.ok(" Successfully updated request with ID: " + updated.getRequestId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("⚠️ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Error updating request: " + e.getMessage());
        }
    }

    @GetMapping("/my-requests")
    public ResponseEntity<?> getCaregiverRequests(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            // Get caregiver elder_id from token
            String caregiverId = jwtUtil.extractElderId(token);

            // Fetch requests where care_id = caregiverId
            List<CareRequest> list = careRequestService.getRequestsForCaregiver(caregiverId);

            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to load requests: " + e.getMessage());
        }
    }

    // Accept a care request
    @PutMapping("/elder/accept/{requestId}")
    public ResponseEntity<?> acceptCareRequest(@PathVariable String requestId) {
        try {
            CareRequest request = careRequestService.getRequestIdById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found with ID: " + requestId));

            request.setStatus("Accepted");
            CareRequest saved = careRequestService.saveRequest(request);

            Elder elder = saved.getElder();
            if (elder == null || elder.getEmail() == null) {
                return ResponseEntity.badRequest().body("Elder email not found.");
            }

            String message = careRequestService.requestEmail(saved, elder);

            return ResponseEntity.ok("Request accepted successfully. " + message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to accept request: " + e.getMessage());
        }
    }

    // Reject a care request
    @PutMapping("/elder/reject/{requestId}")
    public ResponseEntity<?> rejectCareRequest(@PathVariable String requestId) {
        try {
            CareRequest request = careRequestService.getRequestIdById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found with ID: " + requestId));

            request.setStatus("Rejected");
            CareRequest saved = careRequestService.saveRequest(request);

            Elder elder = saved.getElder();
            if (elder == null || elder.getEmail() == null) {
                return ResponseEntity.badRequest().body("Elder email not found.");
            }

            String message = careRequestService.requestEmail(saved, elder);

            return ResponseEntity.ok("Request rejected successfully. " + message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to reject request: " + e.getMessage());
        }
    }

    @GetMapping("/request/records")
    public Map<String, Object> getAllRecords(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Check role
        String role = jwtUtil.extractRole(token);
        if (role == null || !role.equalsIgnoreCase("Elder")) {
            throw new RuntimeException("Access denied: Only Elders can access health records");
        }

        // Extract elder_id from token
        String elderId = jwtUtil.extractElderId(token);

        // Fetch all health records for this elder
        List<CareRequest> records = careRequestService.findAllByElderId(elderId);

        response.put("records", records);
        response.put("count", records.size());

        return response;
    }

}
