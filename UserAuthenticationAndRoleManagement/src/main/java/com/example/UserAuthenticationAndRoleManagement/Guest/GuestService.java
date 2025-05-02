package com.example.UserAuthenticationAndRoleManagement.Guest;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.BookingAndPaymentClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.GuestPropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestPropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.PaymentDTO;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<BookingDTO> getBookingsByGuestId(String guestId) {
        return bookingAndPaymentClient.getBookingsByGuestId(guestId);
    }
    public  Long createBooking(BookingDTO bookingDTO) {
        return bookingAndPaymentClient.createBooking(bookingDTO);
      //  markPropertyAsBooked(bookingDTO.getPropertyId());
    }
    public void markPropertyAsBooked(Long propertyId) {
        GuestPropertyDTO property = guestPropertyClient.getPropertyById(propertyId);
        property.setBooked(true);
        guestPropertyClient.updateProperty(propertyId, property);
    }
    public void cancelBooking(Long bookingId) {
        BookingDTO cancelledBooking = bookingAndPaymentClient.cancelBooking(bookingId);

//        // 2. Update the property to set booked = false
//        GuestPropertyDTO property = guestPropertyClient.getPropertyById(cancelledBooking.getPropertyId());
//        property.setBooked(false);
//         guestPropertyClient.updateProperty(property.getPropertyId(), property);
  }
    public void refundPayment(Long bookingId) {
        PaymentDTO payment = new PaymentDTO();
        payment.setBookingId(bookingId);
        payment.setStatus(PaymentDTO.PaymentStatus.REFUNDED);
        payment.setCreatedAt(LocalDateTime.now());

        // Call the microservice to update payment status
        bookingAndPaymentClient.updatePaymentStatus(payment);
    }
    public List<String> getUnavailableDatesForProperty(Long propertyId) {
        return bookingAndPaymentClient.getBookedDatesByPropertyId(propertyId);
    }
    public  void createPayment(PaymentDTO paymentDTO) {
        bookingAndPaymentClient.createPayment(paymentDTO);
        //  markPropertyAsBooked(bookingDTO.getPropertyId());
    }
    public BookingDTO getBookingById(long bookingId) {
        return bookingAndPaymentClient.getBookingById(bookingId);
    }
    public GuestPropertyDTO getProperty(long propertyId) {
        return guestPropertyClient.getPropertyById(propertyId);
    }
    public PaymentDTO getPayment(long paymentId) {
        return bookingAndPaymentClient.getPaymentById(paymentId);
    }


}