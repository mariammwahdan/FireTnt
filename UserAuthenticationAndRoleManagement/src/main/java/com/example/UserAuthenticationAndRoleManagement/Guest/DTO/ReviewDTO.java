package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    private String guestId;

    private long propertyId;


    private String reviewText;


    private Integer rating;

}

