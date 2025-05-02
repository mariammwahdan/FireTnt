package com.example.UserAuthenticationAndRoleManagement.Guest.Client;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.PaymentDTO;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BookingAndPaymentClient {

    private static final Logger logger = LoggerFactory.getLogger(BookingAndPaymentClient.class);

    private final RestTemplate restTemplate;
    public BookingAndPaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private final String bookingServiceUrl = "http://localhost:8084/api/booking";
    private final String paymentServiceUrl = "http://localhost:8084/api/payment";

    public Long createBooking(BookingDTO bookingDTO) {
String url = bookingServiceUrl + "/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookingDTO> request = new HttpEntity<>(bookingDTO, headers);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, request, Long.class);

        return response.getBody();
    }
    public List<BookingDTO> getBookingsByGuestId(String guestId) {
        String url = bookingServiceUrl + "/user/" + guestId;
        ResponseEntity<List<BookingDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookingDTO>>() {}
        );

        List<BookingDTO> bookings = response.getBody();

        logger.info("Received {} bookings for guestId={}", bookings.size(), guestId);
        for (BookingDTO booking : bookings) {
            logger.info("Booking - ID: {}, PropertyID: {}, GuestID: {}, CheckIn: {}, CheckOut: {}, Price: {}, Status: {}",
                    booking.getBookingId(), booking.getPropertyId(), booking.getGuestId(),
                    booking.getCheckIn(), booking.getCheckOut(), booking.getPrice(), booking.getStatus());
        }

        return response.getBody();

    }
    public BookingDTO cancelBooking(Long id) {
        String url = bookingServiceUrl + "/" + id + "/cancel";
        HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<BookingDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                BookingDTO.class
        );
        return response.getBody();
    }
    public List<String> getBookedDatesByPropertyId(Long propertyId) {
        String url = bookingServiceUrl + "/property/" + propertyId + "/booked-dates";

        ResponseEntity<List<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );

        return response.getBody();
    }
    public BookingDTO getBookingById(Long bookingId) {
        String url = bookingServiceUrl + "/" + bookingId;
        ResponseEntity<BookingDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BookingDTO.class
        );
        return response.getBody();
    }
public PaymentDTO getPaymentById(Long paymentId) {
        String url = paymentServiceUrl + "/" + paymentId;
        ResponseEntity<PaymentDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                PaymentDTO.class
        );
        return response.getBody();
    }
    public void createPayment(PaymentDTO paymentDTO) {
        String url = paymentServiceUrl + "/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentDTO> request = new HttpEntity<>(paymentDTO, headers);
        restTemplate.postForEntity(url,request, Void.class);
    }
    public void updatePaymentStatus(PaymentDTO paymentDTO) {
        String url = paymentServiceUrl +"/"+paymentDTO.getBookingId()+ "/status";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentDTO> request = new HttpEntity<>(paymentDTO, headers);
        restTemplate.postForEntity(url,request, Void.class);
    }

}
