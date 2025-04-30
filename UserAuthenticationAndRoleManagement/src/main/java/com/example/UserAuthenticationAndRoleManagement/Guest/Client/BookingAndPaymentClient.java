package com.example.UserAuthenticationAndRoleManagement.Guest.Client;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BookingAndPaymentClient {


    private final RestTemplate restTemplate;
    public BookingAndPaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private final String bookingServiceUrl = "http://localhost:8084/api/booking";

    public void createBooking(CreateBookingDTO bookingDTO) {
String url = bookingServiceUrl + "/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateBookingDTO> request = new HttpEntity<>(bookingDTO, headers);
        restTemplate.postForEntity(url,request, Void.class);
    }
    public List<Booking> getBookingsByGuestId(String guestId) {
        String url = bookingServiceUrl + "/user/" + guestId;
        ResponseEntity<List<Booking>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Booking>>() {}
        );

        return response.getBody();
    }
    public CreateBookingDTO cancelBooking(Long id) {
        String url = bookingServiceUrl + "/" + id + "/cancel";
        HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<CreateBookingDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CreateBookingDTO.class
        );
        return response.getBody();
    }





}
