package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewDTO {
    private long propertyId;
    private String guestId;
    private String guestName;
    private String reviewText;



    private Integer rating;


}
