package com.example.Properties.Property.DTO;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public class UpdatePropertyDTO {

    @Size(min = 1, message = "Title must not be empty")
    private String title;

    @Size(min = 1, message = "Description must not be empty")
    private String description;

    @Positive(message = "Price per night must be a positive number")
    private Double pricePerNight;

   // @Positive(message = "Host ID must be a positive number")
    private String hostId;

    private Boolean isBooked;

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

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

    @Override
    public String toString() {
        return "UpdatePropertyDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", hostId=" + hostId +
                ", isBooked=" + isBooked +
                '}';
    }
}