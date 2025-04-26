package com.example.Reviews.Review.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateReviewRatingDTO {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
