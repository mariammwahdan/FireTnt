package com.example.Reviews.Review.DTO;
import jakarta.validation.constraints.*;

public class CreateReviewDTO {

    @Positive(message = "Guest ID must be a positive number")
    private long guestId;

    @Positive(message = "Property ID must be a positive number")
    private long propertyId;

    @NotBlank(message = "Review text cannot be blank")
    private String reviewText;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    // Getters and Setters
    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
        this.guestId = guestId;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

