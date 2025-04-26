package com.example.UserAuthenticationAndRoleManagement.Admin;



// at top of AdminService.java
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.transaction.annotation.Transactional;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Payment.Payment;
import com.example.UserAuthenticationAndRoleManagement.Admin.Repository.BookingAndPaymentRepository;
import com.example.UserAuthenticationAndRoleManagement.Admin.Repository.PropertyRepository;
import com.example.UserAuthenticationAndRoleManagement.User.Role;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.example.UserAuthenticationAndRoleManagement.User.UserRepository;

import org.springframework.data.mapping.model.Property;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@Service
public class AdminService {


//    private final UserRepository userRepo;
//    private final BookingAndPaymentRepository bookingRepo;
//    private final RestTemplate rest;
//    private final String propertyUrl;
//
//    public AdminService(
//            UserRepository userRepo,
//            BookingAndPaymentRepository bookingRepo,
//            RestTemplate rest,
//            @Value("${services.property.url}") String propertyUrl
//    ) {
//        this.userRepo = userRepo;
//        this.bookingRepo = bookingRepo;
//        this.rest = rest;
//        this.propertyUrl = propertyUrl;
//    }
//
//    @Transactional
//    public void banUser(Long userId) {
//        User u = userRepo.findById(userId)
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        if (u.getRole() == Role.GUEST) {
//            bookingRepo.cancelByGuestId(userId);
//        }
//
//        if (u.getRole() == Role.HOST) {
//            // fetch raw JSON array of properties for this host
//            JsonNode arr = rest.getForObject(
//                    propertyUrl + "/api/properties/host/{hostId}",
//                    JsonNode.class,
//                    userId
//            );
//
//            if (arr != null && arr.isArray()) {
//                for (JsonNode node : arr) {
//                    // extract the propertyId field from each JSON object
//                    int pid = node.get("propertyId").asInt();
//                    // call the approve (/unpublish) endpoint
//                    rest.put(
//                            propertyUrl + "/api/properties/{propertyId}/approve",
//                            null,
//                            pid
//                    );
//                }
//            }
//        }
//
//        u.setBanned(true);
//        userRepo.save(u);
//    }

    private final UserRepository userRepo;
    private final BookingAndPaymentRepository bookingRepo;
    private final PropertyRepository propertyRepo;

    public AdminService(
            UserRepository userRepo,
            BookingAndPaymentRepository bookingRepo,
            PropertyRepository propertyRepo
    ) {
        this.userRepo = userRepo;
        this.bookingRepo = bookingRepo;
        this.propertyRepo = propertyRepo;
    }

    @Transactional(readOnly = true)
    public List<User> viewAllUsers() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Booking> viewAllBookings() {
        return bookingRepo.findAllBookings();
    }

    @Transactional(readOnly = true)
    public List<Payment> viewAllPayments() {
        return bookingRepo.findAllPayments();
    }

    @Transactional
    public void approveProperty(Long propertyId) {
        propertyRepo.approve(propertyId);
    }

    @Transactional
    public boolean banUser(Long userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (u.getRole() == Role.GUEST) {
            bookingRepo.cancelByGuestId(userId);
        }

        if (u.getRole() == Role.HOST) {
            List<Map<String,Object>> props = propertyRepo.findByHost(userId);
            for (Map<String,Object> p : props) {
                Integer pid = (Integer)p.get("propertyId");
                propertyRepo.approve(Long.valueOf(pid));
            }
        }

        u.setBanned(true);
        userRepo.save(u);
        return true;
    }
}


