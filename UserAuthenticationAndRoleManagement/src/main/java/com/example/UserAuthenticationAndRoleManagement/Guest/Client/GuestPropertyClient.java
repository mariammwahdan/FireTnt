package com.example.UserAuthenticationAndRoleManagement.Guest.Client;


import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.CreateReviewDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestPropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GuestPropertyClient {

    private final RestTemplate restTemplate;

    public GuestPropertyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String propertyServiceUrl = "http://localhost:8082/api/properties";

    public GuestPropertyDTO getPropertyById(long id) {
        String url = propertyServiceUrl + "/" + id;
        return restTemplate.getForObject(url, GuestPropertyDTO.class);
    }
    public void updateProperty(long id, GuestPropertyDTO dto) {
        String url = propertyServiceUrl + "/" + id +"/update";
        HttpEntity<GuestPropertyDTO> request = new HttpEntity<>(dto);
        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
    }
    public void createReview(long propertyId, CreateReviewDTO reviewDTO) {
        String url = propertyServiceUrl + "/" + propertyId + "/reviews";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateReviewDTO> request = new HttpEntity<>(reviewDTO, headers);

        restTemplate.postForEntity(url, request, Void.class);
    }

}
