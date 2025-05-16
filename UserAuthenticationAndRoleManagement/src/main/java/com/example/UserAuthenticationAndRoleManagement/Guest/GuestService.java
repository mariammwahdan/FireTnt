package com.example.UserAuthenticationAndRoleManagement.Guest;
import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.BookingAndPaymentClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.GuestPropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.NotificationsClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.CreateReviewDTO;
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
    private final NotificationsClient notificationsClient;
    GuestPropertyClient guestPropertyClient;
    UserService userService;
    @Autowired
    public GuestService(BookingAndPaymentClient bookingAndPaymentClient, NotificationsClient notificationsClient, GuestPropertyClient guestPropertyClient, UserService userService) {
        this.bookingAndPaymentClient = bookingAndPaymentClient;
        this.notificationsClient = notificationsClient;
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

    }

    public void cancelBooking(Long bookingId) {
        bookingAndPaymentClient.cancelBooking(bookingId);
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


    public Long bookPayAndNotify(BookingDTO bookingDto, PaymentDTO paymentDto) {
        // 1) create the booking
        Long bookingId = bookingAndPaymentClient.createBooking(bookingDto);

        // 2) process the payment (attach the bookingId to the payment DTO)
        paymentDto.setBookingId(bookingId);
        bookingAndPaymentClient.createPayment(paymentDto);

        // 3) fire off the notification
        //    - resolve our internal guestId & email
        Long guestId = userService.findUserIdByFirebaseUid(bookingDto.getGuestId());
        String email = userService.fetchById(guestId).getEmail();

        //    - build a friendly message
        String msg = String.format(
                "Thank you for your payment for property %d! Booking ref: %d",
                bookingDto.getPropertyId(),
                bookingId
        );
        CreateNotificationDTO notifDto =
                new CreateNotificationDTO(guestId, email, msg);

        //    - call the Notifications microservice
        notificationsClient.createNotification(notifDto);

        return bookingId;
    }
    public void createReview(CreateReviewDTO createReviewDTO) {
        guestPropertyClient.createReview(createReviewDTO);
    }
}