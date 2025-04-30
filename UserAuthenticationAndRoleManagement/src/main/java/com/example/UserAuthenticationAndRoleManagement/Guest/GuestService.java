package com.example.UserAuthenticationAndRoleManagement.Guest;

import com.example.BookingAndPayment.Aspects.LoggingAspect;
import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.BookingAndPaymentClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.GuestPropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestPropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    BookingAndPaymentClient bookingAndPaymentClient;
    GuestPropertyClient guestPropertyClient;
    UserService userService;
    @Autowired
    public GuestService(BookingAndPaymentClient bookingAndPaymentClient, GuestPropertyClient guestPropertyClient, UserService userService) {
        this.bookingAndPaymentClient = bookingAndPaymentClient;
        this.guestPropertyClient = guestPropertyClient;
        this.userService = userService;

    }
public GuestPropertyDTO getPropertyById(long propertyId) {
        return guestPropertyClient.getPropertyById(propertyId);
    }
    public List<Booking> getBookingsByGuestId(String guestId) {
        return bookingAndPaymentClient.getBookingsByGuestId(guestId);
    }
    public  void createBooking(CreateBookingDTO bookingDTO) {
        bookingAndPaymentClient.createBooking(bookingDTO);
        markPropertyAsBooked(bookingDTO.getPropertyId());
    }
    public void markPropertyAsBooked(Long propertyId) {
        GuestPropertyDTO property = guestPropertyClient.getPropertyById(propertyId);
        property.setBooked(true);
        guestPropertyClient.updateProperty(propertyId, property);
    }
    public CreateBookingDTO cancelBooking(Long bookingId) {
        return bookingAndPaymentClient.cancelBooking(bookingId);
    }
}