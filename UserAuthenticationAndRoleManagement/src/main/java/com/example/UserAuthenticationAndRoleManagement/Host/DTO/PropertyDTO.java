package com.example.UserAuthenticationAndRoleManagement.Host.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDTO {

    // Property attributes

    private long id;
    private String title;
    private String description;
    private double pricePerNight;
    private boolean isBooked;
    private String hostId;

    // Constructors
    public PropertyDTO() {
    }
    public PropertyDTO(long id, String title, String description, double pricePerNight, boolean isBooked, String hostId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.isBooked = isBooked;
        this.hostId = hostId;
    }

    // Getters & Setters


}