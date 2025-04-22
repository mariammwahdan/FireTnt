package com.example.Properties.Property.DTO;

import jakarta.validation.constraints.*;


public class CreatePropertyDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price per night must be a positive number")
    private double pricePerNight;

    @NotNull(message = "Is booked status is required")
    private boolean isBooked;

    @Positive(message = "Host ID must be a positive number")
    private Long hostId; // Foreign key reference to User Microservice
    // Getters and Setters



    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPricePerNight() {
        return pricePerNight;
    }
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isBooked() {
        return isBooked;
    }
    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }
    public Long getHostId() {
        return hostId;
    }
    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }




}
