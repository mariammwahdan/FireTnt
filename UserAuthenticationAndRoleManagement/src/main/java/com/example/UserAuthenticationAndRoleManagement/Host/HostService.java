package com.example.UserAuthenticationAndRoleManagement.Host;

import com.example.UserAuthenticationAndRoleManagement.Host.Client.PropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import java.util.List;

@Service
public class HostService {
    private final RestTemplate restTemplate;
    private final PropertyClient propertyClient;
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
public String getRoleName(){
        return userService.getRoleName();
}

//    public String getRoleNameFromPrincipal() {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.getPrincipal() instanceof FirebasePrincipal fp) {
//            User user = userService.findByEmail(fp.getEmail());
//            return user.getRole().name();
//        }
//        throw new IllegalStateException("No valid FirebasePrincipal in context");
//    }


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
    public void deleteProperty(Integer id) {
        propertyClient.deleteProperty(id);
    }

    public List<PropertyDTO> getAllProperties() {
        return propertyClient.getProperties();
    }
}