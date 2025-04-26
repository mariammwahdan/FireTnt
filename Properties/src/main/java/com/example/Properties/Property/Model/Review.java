package com.example.Properties.Property.Model;


public class Review {
    private long id;
    private long guestId;
    private long propertyId;
    private String reviewText;
    private int rating;

    public Review() {
    }
    public Review(long guestId, long propertyId, String reviewText, int rating) {
        this.guestId = guestId;
        this.propertyId = propertyId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
