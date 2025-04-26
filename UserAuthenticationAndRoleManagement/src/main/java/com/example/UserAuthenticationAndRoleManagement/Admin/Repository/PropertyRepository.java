package com.example.UserAuthenticationAndRoleManagement.Admin.Repository;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Repository
public class PropertyRepository {

    private final RestTemplate rest;
    @Value("${services.property.url}")
    private String propertyUrl;

    public PropertyRepository(RestTemplate rest) {
        this.rest = rest;
    }

    public List<Map<String, Object>> findByHost(Long hostId) {
        return rest.exchange(
                propertyUrl + "/api/properties/host/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                },
                hostId
        ).getBody();
    }

    public void approve(Long propertyId) {
        rest.put(
                propertyUrl + "/api/properties/{id}/approve",
                null,
                propertyId
        );
    }

    public void unpublish(Long propertyId) {
        rest.put(propertyUrl + "/api/properties/{id}/approve", null, propertyId);
    }
}

