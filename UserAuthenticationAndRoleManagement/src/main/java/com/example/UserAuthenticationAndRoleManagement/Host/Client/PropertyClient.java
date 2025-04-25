package com.example.UserAuthenticationAndRoleManagement.Host.Client;


import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class PropertyClient {

    private final WebClient propertyWebClient;

    public PropertyClient(WebClient propertyWebClient) {
        this.propertyWebClient = propertyWebClient;
    }

    public List<PropertyDTO> getPropertiesByHostId(Long hostId) {
        return propertyWebClient.get()
                .uri("/host/{hostId}", hostId) // Your property microservice endpoint
                .retrieve()
                .bodyToFlux(PropertyDTO.class)
                .collectList()
                .block(); // Blocking here for simplicity. Use reactive if needed.
    }
}