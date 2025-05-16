package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    private String guestId;
private String guestName;
    private long propertyId;


    private String reviewText;


    private Integer rating;
    @Override
    public String toString() {
        return "ReviewDTO{" +
                "guestName='" + guestName + '\'' +
                ", comment='" + reviewText + '\'' +
                ", rating=" + rating +
                '}';
    }


}

