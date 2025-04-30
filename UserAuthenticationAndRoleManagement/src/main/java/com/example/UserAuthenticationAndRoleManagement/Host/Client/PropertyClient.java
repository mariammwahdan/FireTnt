package com.example.UserAuthenticationAndRoleManagement.Host.Client;


import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class PropertyClient {

    private final RestTemplate restTemplate;

    public PropertyClient(RestTemplate restTemplate) {
        this.restTemplate=restTemplate;
    }

   private final String propertyServiceUrl = "http://localhost:8082/api/properties";

    public List<PropertyDTO> getPropertiesByHostId(String hostId) {
        String url = propertyServiceUrl + "/host/" + hostId;

        ResponseEntity<List<PropertyDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PropertyDTO>>() {}
        );

        return response.getBody();
    }


    public void createProperty(PropertyDTO propertyDTO) {
        String url = propertyServiceUrl + "/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PropertyDTO> request = new HttpEntity<>(propertyDTO, headers);

        restTemplate.postForEntity(url, request, Void.class);}

    public PropertyDTO getPropertyById(Integer id) {
        String url = propertyServiceUrl + "/" + id;
        return restTemplate.getForObject(url, PropertyDTO.class);
    }

    public void updateProperty(Integer id, PropertyDTO dto) {
        String url = propertyServiceUrl + "/" + id +"/update";
        HttpEntity<PropertyDTO> request = new HttpEntity<>(dto);
        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
    }

}