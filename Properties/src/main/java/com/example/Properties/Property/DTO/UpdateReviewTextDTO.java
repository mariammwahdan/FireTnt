package com.example.Properties.Property.DTO;

import jakarta.validation.constraints.NotBlank;

public class UpdateReviewTextDTO {

    @NotBlank(message = "Review text cannot be blank")
    private String reviewText;

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
