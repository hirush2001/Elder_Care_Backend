package com.eldercare.eldercare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eldercare.eldercare.model.CareGiver;
import com.eldercare.eldercare.model.CareRequest;
import com.eldercare.eldercare.model.DailyHealthRecord;
import com.eldercare.eldercare.model.Elder;
import com.eldercare.eldercare.model.CareTakerReview;
import com.eldercare.eldercare.repository.ElderProfileRepository;
import com.eldercare.eldercare.repository.ElderRepository;
import com.eldercare.eldercare.repository.CareRequestRepository;
import com.eldercare.eldercare.repository.CareTakerRepository;
import com.eldercare.eldercare.repository.CareRequestReviewRepository;


@Service
public class ReviewService {
    private final CareRequestRepository careRequestRepository;
    private final CareTakerRepository careTakerRepository;
    private final CareRequestReviewRepository careRequestReviewRepository;
    private final ElderRepository elderRepository;
    private final EmailService emailService;

    public ReviewService(CareRequestRepository careRequestRepository,
            CareTakerRepository careTakerRepository, CareRequestReviewRepository careRequestReviewRepository, 
            ElderRepository elderRepository, EmailService emailService) {
        this.careRequestRepository = careRequestRepository;
        this.careTakerRepository = careTakerRepository;
        this.careRequestReviewRepository = careRequestReviewRepository;
        this.elderRepository = elderRepository;
        this.emailService = emailService;
    }

    public CareTakerReview saveReview(CareTakerReview careTakerReview) {
        return careRequestReviewRepository.save(careTakerReview);
    }

    public String generateReviewId() {
        List<CareTakerReview> records = careRequestReviewRepository.findAll();

        if (records.isEmpty()) {
            return "Review001";
        }

        CareTakerReview lastRecord = records.get(records.size() - 1);
        String lastId = lastRecord.getReviewId();
        int num = Integer.parseInt(lastId.substring(6)); // remove 'Review' (6 characters)
        num++;
        return String.format("Review%03d", num);
    }

    public CareGiver findCareGiverById(String careTakerId) {
        return careTakerRepository.findById(careTakerId).orElse(null);
    }

    // ✅ Find Elder by ID
    public Elder findElderById(String elderId) {
        return elderRepository.findById(elderId).orElse(null);
    }

    // ✅ Get all care requests
    public List<CareTakerReview> getAllRequests() {
        return careRequestReviewRepository.findAll();
    }

    // ✅ Get single request
    public Optional<CareTakerReview> getRequestIdById(String reviewId) {
        return careRequestReviewRepository.findById(reviewId);
    }

    // ✅ Delete care request
    public void deleteRequest(String reviewId) {
        if (!careRequestReviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Care request review not found with ID: " + reviewId);
        }
        careRequestReviewRepository.deleteById(reviewId);
    }

    // ✅ Get reviews by care ID
    public List<CareTakerReview> getReviewsByCareId(String careId) {
        return careRequestReviewRepository.findByCareIdAndFetchElder(careId);
    }

    // ✅ Get reviews by elder ID
    public List<CareTakerReview> getReviewsByElderId(String elderId) {
        return careRequestReviewRepository.findAllByElder_ElderId(elderId);
    }

    // ✅ Get average rating by care ID
    public Double getAverageRatingByCareId(String careId) {
        List<CareTakerReview> reviews = careRequestReviewRepository.findByCareIdAndFetchElder(careId);
        
        if (reviews.isEmpty()) {
            return null; // No reviews found
        }
        
        double sum = 0.0;
        int count = 0;
        
        for (CareTakerReview review : reviews) {
            try {
                double rating = Double.parseDouble(review.getRate());
                sum += rating;
                count++;
            } catch (NumberFormatException e) {
                // Skip invalid ratings
            }
        }
        
        return count > 0 ? sum / count : null;
    }

}
