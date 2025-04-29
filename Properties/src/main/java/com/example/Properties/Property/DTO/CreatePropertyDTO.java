package com.example.Properties.Property.DTO;

import jakarta.validation.constraints.*;


public class CreatePropertyDTO {
    private Long propertyId;
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price per night must be a positive number")
    private double pricePerNight;

    @NotNull(message = "Is booked status is required")
    private boolean isBooked;

    @Positive(message = "Host ID must be a positive number")
    private String hostId; // Foreign key reference to User Microservice
    // Getters and Setters

public CreatePropertyDTO() {}
    public CreatePropertyDTO(Long propertyId, String title, String description,
                       double pricePerNight, boolean isBooked, String hostId) {
        this.propertyId = propertyId;
        this.title = title;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.isBooked = isBooked;
        this.hostId = hostId;
    }
    public long getPropertyId() {
        return propertyId;
    }
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
    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }




}
