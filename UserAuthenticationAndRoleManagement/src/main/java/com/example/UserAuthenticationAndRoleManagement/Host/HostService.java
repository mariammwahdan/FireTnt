package com.example.UserAuthenticationAndRoleManagement.Host;


import com.example.UserAuthenticationAndRoleManagement.Config.RestTemplateConfig;
import com.example.UserAuthenticationAndRoleManagement.Host.Client.PropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.example.UserAuthenticationAndRoleManagement.User.UserRepository;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Service
public class HostService {
    private final RestTemplate restTemplate;
    private final PropertyClient propertyClient;
    private static final String PROPERTY_SERVICE_URL = "http://localhost:8082/api/properties";
    UserService userService;

    @Autowired
    public HostService(PropertyClient propertyClient, RestTemplate restTemplate,UserService userService) {
        this.restTemplate = restTemplate;
        this.propertyClient = propertyClient;
        this.userService = userService;

    }
    public String getUidFromPrincipal(Principal principal) {
        if (principal instanceof Authentication auth &&
                auth.getPrincipal() instanceof FirebasePrincipal firebasePrincipal) {
            return firebasePrincipal.getUid(); // ✅ This is the Firebase UID
        }
        throw new IllegalStateException("Invalid principal type");
    }


    public String getRoleNameFromPrincipal(Principal principal) {
        if (principal instanceof FirebasePrincipal fp) {
            User user = userService.findByEmail(fp.getEmail());
            return user.getRole().name();
        }
        return "GUEST"; // fallback
    }

    // ###################################################



    // Endpoints that call the Properties service
    public List<PropertyDTO> getPropertiesForHost(String hostId) {
        return propertyClient.getPropertiesByHostId(hostId);
    }
    public void createProperty(PropertyDTO dto) {
        propertyClient.createProperty(dto);
    }
    public PropertyDTO getPropertyById(Integer id) {
        return propertyClient.getPropertyById(id);
    }

    public void updateProperty(Integer id, PropertyDTO dto) {
        propertyClient.updateProperty(id, dto);
    }


}