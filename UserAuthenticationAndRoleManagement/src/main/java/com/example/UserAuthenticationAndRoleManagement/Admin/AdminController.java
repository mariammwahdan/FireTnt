package com.example.UserAuthenticationAndRoleManagement.Admin;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Payment.Payment;
import com.example.UserAuthenticationAndRoleManagement.Host.HostService;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService admin;
    private final HostService hostService;
    public AdminController(AdminService admin, HostService hostService) {
        this.admin = admin;
        this.hostService = hostService;
    }

    @GetMapping("/admin")
    public String showAdminDashboard(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<User> users = (keyword == null || keyword.isEmpty())
                ? admin.viewAllUsersExceptADMIN()
                : admin.searchUsers(keyword);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", hostService.getRoleName());
        return "admin-dashboard";
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

    @GetMapping("/users/{id}/ban")
    public String ban(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        admin.banUser(id);
        redirectAttributes.addFlashAttribute("success", "User banned successfully!");
        return "redirect:/api/admin/admin";
    }
    @GetMapping("/users/{id}/unban")
    public String unban(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        admin.unbanUser(id);
        redirectAttributes.addFlashAttribute("success", "User unbanned successfully!");
        return "redirect:/api/admin/admin";
    }
}
