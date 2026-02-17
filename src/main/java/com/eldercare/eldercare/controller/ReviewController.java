package com.eldercare.eldercare.controller;

import com.eldercare.eldercare.config.JwtUtil;
import com.eldercare.eldercare.model.CareTakerReview;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Create a new review
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody CareTakerReview careTakerReview, 
                                         @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract JWT token from Authorization header
            String token = authHeader.replace("Bearer ", "");
            
            // Extract elder_id from JWT token
            String elderId = jwtUtil.extractElderId(token);
            
            if (elderId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired token"));
            }
            
            // Get Elder entity using the extracted elder_id
            Elder elder = reviewService.findElderById(elderId);
            if (elder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Elder not found with ID: " + elderId));
            }
            
            // Set the elder in the review (automatically from JWT)
            careTakerReview.setElder(elder);
            
            // Generate review ID
            careTakerReview.setReviewId(reviewService.generateReviewId());
            
            // Save the review
            CareTakerReview savedReview = reviewService.saveReview(careTakerReview);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                        "message", "Review created successfully",
                        "review", savedReview
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create review: " + e.getMessage()));
        }
    }

    /**
     * Get all reviews
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllReviews() {
        try {
            List<CareTakerReview> reviews = reviewService.getAllRequests();
            return ResponseEntity.ok(Map.of(
                "message", "Reviews retrieved successfully",
                "reviews", reviews,
                "count", reviews.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve reviews: " + e.getMessage()));
        }
    }

    /**
     * Get review by ID
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable String reviewId) {
        try {
            Optional<CareTakerReview> review = reviewService.getRequestIdById(reviewId);
            
            if (review.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "message", "Review found",
                    "review", review.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Review not found with ID: " + reviewId));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve review: " + e.getMessage()));
        }
    }

    /**
     * Get reviews by care ID
     */
    @GetMapping("/care/{careId}")
    public ResponseEntity<?> getReviewsByCareId(@PathVariable String careId) {
        try {
            // Note: You'll need to add this method to ReviewService
            List<CareTakerReview> reviews = reviewService.getReviewsByCareId(careId);
            return ResponseEntity.ok(Map.of(
                "message", "Reviews for care ID retrieved successfully",
                "reviews", reviews,
                "count", reviews.size(),
                "careId", careId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve reviews by care ID: " + e.getMessage()));
        }
    }

    /**
     * Get reviews by elder ID
     */
    @GetMapping("/elder/{elderId}")
    public ResponseEntity<?> getReviewsByElderId(@PathVariable String elderId) {
        try {
            List<CareTakerReview> reviews = reviewService.getReviewsByElderId(elderId);
            return ResponseEntity.ok(Map.of(
                "message", "Reviews for elder retrieved successfully",
                "reviews", reviews,
                "count", reviews.size(),
                "elderId", elderId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve reviews by elder ID: " + e.getMessage()));
        }
    }

    /**
     * Update an existing review
     */
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable String reviewId, @RequestBody CareTakerReview updatedReview) {
        try {
            Optional<CareTakerReview> existingReview = reviewService.getRequestIdById(reviewId);
            
            if (!existingReview.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Review not found with ID: " + reviewId));
            }

            // Preserve the review ID and elder relationship
            updatedReview.setReviewId(reviewId);
            if (updatedReview.getElder() == null) {
                updatedReview.setElder(existingReview.get().getElder());
            }
            
            CareTakerReview savedReview = reviewService.saveReview(updatedReview);
            
            return ResponseEntity.ok(Map.of(
                "message", "Review updated successfully",
                "review", savedReview
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update review: " + e.getMessage()));
        }
    }

    /**
     * Delete a review
     */
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable String reviewId) {
        try {
            reviewService.deleteRequest(reviewId);
            return ResponseEntity.ok(Map.of(
                "message", "Review deleted successfully",
                "reviewId", reviewId
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete review: " + e.getMessage()));
        }
    }

    /**
     * Get average rating for a specific care giver
     */
    @GetMapping("/average-rating/{careId}")
    public ResponseEntity<?> getAverageRating(@PathVariable String careId) {
        try {
            Double averageRating = reviewService.getAverageRatingByCareId(careId);
            return ResponseEntity.ok(Map.of(
                "message", "Average rating calculated successfully",
                "careId", careId,
                "averageRating", averageRating != null ? averageRating : 0.0,
                "hasReviews", averageRating != null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to calculate average rating: " + e.getMessage()));
        }
    }

    /**
     * Get review statistics for a care giver
     */
    @GetMapping("/stats/{careId}")
    public ResponseEntity<?> getReviewStats(@PathVariable String careId) {
        try {
            List<CareTakerReview> reviews = reviewService.getReviewsByCareId(careId);
            
            int totalReviews = reviews.size();
            double averageRating = 0.0;
            
            if (totalReviews > 0) {
                double sum = reviews.stream()
                    .mapToDouble(review -> {
                        try {
                            return Double.parseDouble(review.getRate());
                        } catch (NumberFormatException e) {
                            return 0.0;
                        }
                    })
                    .sum();
                averageRating = sum / totalReviews;
            }
            
            return ResponseEntity.ok(Map.of(
                "message", "Review statistics retrieved successfully",
                "careId", careId,
                "totalReviews", totalReviews,
                "averageRating", Math.round(averageRating * 100.0) / 100.0,
                "reviews", reviews
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve review statistics: " + e.getMessage()));
        }
    }
}
