package com.example.Properties.Property.Model;


public class Review {
    private long id;
    private String guestId;
    private String guestName;
    private long propertyId;
    private String reviewText;
    private Integer rating;

    public Review() {
    }

    public Review(String guestName, String guestId, long propertyId, String reviewText, int rating) {
        this.guestName = guestName;
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

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getGuestName() {
        return guestName;
    }
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    @Override
    public String toString() {
        return "ReviewDTO{" +
                "guestName='" + guestName + '\'' +
                ", comment='" + reviewText + '\'' +
                ", rating=" + rating +
                '}';
    }

}
