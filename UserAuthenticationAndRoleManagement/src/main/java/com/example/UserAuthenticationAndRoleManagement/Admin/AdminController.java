package com.example.UserAuthenticationAndRoleManagement.Admin;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Payment.Payment;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService admin;

    public AdminController(AdminService admin) {
        this.admin = admin;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return admin.viewAllUsers();
    }

    @GetMapping("/bookings")
    public List<Booking> allBookings() {
        return admin.viewAllBookings();
    }

    @GetMapping("/payments")
    public List<Payment> allPayments() {
        return admin.viewAllPayments();
    }

    @PostMapping("/properties/{id}/approve")
    public void approve(@PathVariable Long id) {
        admin.approveProperty(id);
    }

    @PostMapping("/users/{id}/ban")
    public void ban(@PathVariable Long id) {
        admin.banUser(id);
    }
}
