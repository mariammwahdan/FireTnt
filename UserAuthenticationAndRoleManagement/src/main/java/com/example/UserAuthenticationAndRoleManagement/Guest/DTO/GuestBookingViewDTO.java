package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestBookingViewDTO {
    private Booking booking;
    private String propertyTitle;
    private String propertyDescription;


}
