package com.example.UserAuthenticationAndRoleManagement.Host.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePropertyDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Positive
    private double pricePerNight;
    @NotNull
    private boolean isBooked;
    @NotNull
    private Long hostId;

    // Getters and Setters
}
