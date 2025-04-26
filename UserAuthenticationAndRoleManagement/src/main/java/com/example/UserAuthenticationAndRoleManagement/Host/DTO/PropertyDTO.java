package com.example.UserAuthenticationAndRoleManagement.Host.DTO;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyDTO {

    // Property attributes

    private Long id;
    private String title;
    private String description;
    private double pricePerNight;
    private boolean isBooked;
    private Long hostId;

    // Getters & Setters


}