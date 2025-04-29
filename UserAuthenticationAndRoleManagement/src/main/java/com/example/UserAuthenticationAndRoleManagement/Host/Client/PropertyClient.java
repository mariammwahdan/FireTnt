package com.example.UserAuthenticationAndRoleManagement.Host.Client;


import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public List<PropertyDTO> getAllProperties() {
        PropertyDTO[] propertiesArray = restTemplate.getForObject(propertyServiceUrl, PropertyDTO[].class);
        return List.of(propertiesArray);
    }
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
        restTemplate.postForEntity(url, propertyDTO, Void.class);
    }
    public void updateProperty(long propertyId, PropertyDTO propertyDTO) {
        String url = propertyServiceUrl + "/update/" + propertyId;
        restTemplate.put(url, propertyDTO);
    }
}