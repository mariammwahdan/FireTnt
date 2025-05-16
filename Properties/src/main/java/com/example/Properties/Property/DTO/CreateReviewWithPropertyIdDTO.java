package com.example.Properties.Property.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReviewWithPropertyIdDTO {

    @NotNull
    private String guestId;

    @NotBlank
    private String reviewText;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    private long propertyId;
    private String guestName;



    public @NotNull String getGuestId() {
        return guestId;
    }

    public void setGuestId(@NotNull String guestId) {
        this.guestId = guestId;
    }

    public @NotBlank String getReviewText() {
        return reviewText;
    }

    public void setReviewText(@NotBlank String reviewText) {
        this.reviewText = reviewText;
    }

    public @NotNull @Min(1) @Max(5) Integer getRating() {
        return rating;
    }

    public void setRating(@NotNull @Min(1) @Max(5) Integer rating) {
        this.rating = rating;
    }
    public long getPropertyId() {
        return propertyId;
    }
    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
    public String getGuestName() {
        return guestName;
    }
}
