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
import com.eldercare.eldercare.service.CareRequestService;
import com.eldercare.eldercare.service.UserService;
import com.eldercare.eldercare.model.Elder;

import java.util.List;
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

    @PostMapping("/request/{care_taker_id}")
    public ResponseEntity<?> addCareReq(
            @RequestBody CareRequest careRequest,
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("care_taker_id") String careTakerId) {

        try {
            // 1. Extract JWT token (Bearer token)
            String token = authHeader.substring(7);

            // 2. Extract Elder ID from token
            String elderId = jwtUtil.extractElderId(token);

            // 3. Fetch Elder and CareGiver
            Elder elder = userService.findById(elderId);
            CareGiver careGiver = careRequestService.findCareGiverById(careTakerId);

            if (careGiver == null) {
                return ResponseEntity.badRequest().body("Invalid Care Taker ID: " + careTakerId);
            }

            // 4. Generate new Request ID
            String newCareReqId = careRequestService.generateCareReqId();

            // 5. Set all required fields
            careRequest.setRequestId(newCareReqId);
            careRequest.setElder(elder);
            careRequest.setCareGiver(careGiver);

            if (careRequest.getRequestDate() == null || careRequest.getRequestDate().isEmpty()) {
                String today = java.time.LocalDate.now().toString();
                careRequest.setRequestDate(today);
            }

            careRequest.setStatus("Pending");
            // 6. Save to database
            CareRequest saved = careRequestService.saveRequest(careRequest);

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
                return ResponseEntity.ok(" No care requests found.");
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

}
