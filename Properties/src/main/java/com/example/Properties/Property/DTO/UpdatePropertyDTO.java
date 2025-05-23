package com.example.Properties.Property.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public class UpdatePropertyDTO {

    @Size(min = 1, message = "Title must not be empty")
    private String title;

    @Size(min = 1, message = "Description must not be empty")
    private String description;

    @Positive(message = "Price per night must be a positive number")
    private Double pricePerNight;

    private String location;

    private String propertyType;
   // @Positive(message = "Host ID must be a positive number")
    private String hostId;

    private boolean booked;

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

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public boolean getBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getPropertyType() {
        return propertyType;
    }
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    @Override
    public String toString() {
        return "UpdatePropertyDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", hostId=" + hostId +
                ", Booked=" + booked +
                '}';
    }
}