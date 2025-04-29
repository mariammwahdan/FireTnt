package com.example.UserAuthenticationAndRoleManagement.Host.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDTO {

    // Property ID
    private long propertyId;
    // Property attributes
    private String title;
    private String description;
    private double pricePerNight;
    @Setter
    private boolean booked;
    private String hostId;

    // Constructors
    public PropertyDTO() {
    }
    public PropertyDTO( long propertyId,String title, String description, double pricePerNight, boolean booked, String hostId) {
        this.propertyId = propertyId;
        this.title = title;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.booked = booked;
        this.hostId = hostId;
    }

    // Getters & Setters
public boolean getBooked() {
        return this.booked;
    }


}