package com.eldercare.eldercare.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "caretaker_review")

public class CareTakerReview {
        @Id
    @Column(name = "Review_Id", nullable = false, unique = true)
    private String reviewId;
    @Column(name = "Review")
    private String review;
    @Column(name = "Rate")
    private String rate;
    @Column(name = "Care_Id")
    private String careId;

    // 🔹 Many care requests belong to one Elder
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "careRequests" })

    @JoinColumn(name = "Elder_Id", referencedColumnName = "Elder_Id")

    private Elder elder;

        public CareTakerReview(String reviewId, String review, String rate, String careId) 
        {
        this.reviewId = reviewId;
        this.review = review;
        this.rate = rate;
        this.careId = careId;
        }

}
