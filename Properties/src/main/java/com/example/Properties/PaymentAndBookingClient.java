package com.example.Properties;

import com.example.Properties.Property.DTO.PropertyBookingDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PaymentAndBookingClient {


    private final RestTemplate restTemplate;

    public PaymentAndBookingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String bookingServiceUrl = "http://localhost:8084/api/booking";
    private final String paymentServiceUrl = "http://localhost:8084/api/payment";

    public List<PropertyBookingDTO> getAllBookingsForProperty(long propertyId) {
        String url = bookingServiceUrl + "/property/"+propertyId;
        ResponseEntity<List<PropertyBookingDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PropertyBookingDTO>>() {});
        return response.getBody();
    }

    public  void deletBookingsByPropertyId(long propertyId) {
        String url = bookingServiceUrl + "/property/"+propertyId;
        restTemplate.delete(url);
    }
    public void deletePaymentsByBookingId(long bookingId) {
        String url = paymentServiceUrl + "/booking/" + bookingId;
        restTemplate.delete(url);
    }

}
