package com.example.UserAuthenticationAndRoleManagement.Host;

import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.ReviewDTO;
import com.example.UserAuthenticationAndRoleManagement.Host.Client.PropertyClient;
import com.example.UserAuthenticationAndRoleManagement.Host.DTO.PropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import java.util.ArrayList;
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
    public List<BookingDTO> getAllBookingsForHost(String hostId) {
        List<PropertyDTO> properties = getPropertiesForHost(hostId);
        List<BookingDTO> allBookings = new ArrayList<>();
        for (PropertyDTO property : properties) {
            List<BookingDTO> bookings = propertyClient.getBookingsByPropertyId(property.getPropertyId());
            allBookings.addAll(bookings);
        }
        return allBookings;
    }
    public List<ReviewDTO> getReviewsByPropertyId(Integer propertyId) {
        System.out.println(propertyClient.getAllReviewsByPropertyId(propertyId));
        return propertyClient.getAllReviewsByPropertyId(propertyId);
    }


}