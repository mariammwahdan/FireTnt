package com.example.UserAuthenticationAndRoleManagement.Admin.Repository;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class BookingAndPaymentRepository {

    private final RestTemplate rest;
    @Value("${services.booking.url}")
    private String bookingUrl;

    public BookingAndPaymentRepository(RestTemplate rest) {
        this.rest = rest;
    }

    public void cancelByGuestId(Long guestId) {
        rest.delete(bookingUrl + "/api/booking/guest/{id}", guestId);
    }

    public List<Booking> findAllBookings() {
        return rest.exchange(
                bookingUrl + "/api/booking",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Booking>>() {}
        ).getBody();
    }

    public List<Payment> findAllPayments() {
        return rest.exchange(
                bookingUrl + "/api/payments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Payment>>() {}
        ).getBody();
    }
}
