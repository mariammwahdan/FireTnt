package com.example.UserAuthenticationAndRoleManagement.Guest.Client;
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

    public void createBooking(BookingDTO bookingDTO) {
String url = bookingServiceUrl + "/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookingDTO> request = new HttpEntity<>(bookingDTO, headers);
        restTemplate.postForEntity(url,request, Void.class);
    }
    public List<BookingDTO> getBookingsByGuestId(String guestId) {
        String url = bookingServiceUrl + "/user/" + guestId;
        ResponseEntity<List<BookingDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookingDTO>>() {}
        );

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

}
