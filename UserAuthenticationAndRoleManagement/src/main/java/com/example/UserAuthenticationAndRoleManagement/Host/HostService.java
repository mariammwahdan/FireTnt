package com.example.UserAuthenticationAndRoleManagement.Host;


import com.example.UserAuthenticationAndRoleManagement.Config.RestTemplateConfig;
import com.example.UserAuthenticationAndRoleManagement.Host.Client.PropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.CreatePropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class HostService {
    private final RestTemplate restTemplate;
    private final PropertyClient propertyClient;
    private static final String PROPERTY_SERVICE_URL = "http://localhost:8082/api/properties";


    @Autowired
    public HostService(PropertyClient propertyClient, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.propertyClient = propertyClient;
    }
    public List<PropertyDTO> getPropertiesForHost(Long hostId) {
        String url = PROPERTY_SERVICE_URL + "/host/" + hostId;
        PropertyDTO[] response = restTemplate.getForObject(url, PropertyDTO[].class);
        return Arrays.asList(response);
    }

    public void createProperty(CreatePropertyDTO dto) {
        String url = PROPERTY_SERVICE_URL + "/create";
        restTemplate.postForObject(url, dto, Void.class);
    }

}