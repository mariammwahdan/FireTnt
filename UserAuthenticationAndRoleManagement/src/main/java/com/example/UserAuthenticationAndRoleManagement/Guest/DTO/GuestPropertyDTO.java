package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestPropertyDTO {

    // Property ID
    private long propertyId;
    // Property attributes
    private String title;
    private String description;
    private double pricePerNight;
    @Setter
    private boolean booked;
    // Location and type of property
    private String location;
    private String propertyType;
    private String hostId;


    // Constructors
    public GuestPropertyDTO() {
    }
    public GuestPropertyDTO( long propertyId,String title, String description, double pricePerNight, boolean booked,String location,String propertyType, String hostId) {
        this.propertyId = propertyId;
        this.title = title;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.booked = booked;
        this.location = location;
        this.propertyType = propertyType;
        this.hostId = hostId;
    }

    // Getters & Setters
    public boolean getBooked() {
        return this.booked;
    }


}