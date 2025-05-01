package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestBookingViewDTO {
    private BookingDTO booking;
    private String propertyTitle;
    private String propertyDescription;


}
