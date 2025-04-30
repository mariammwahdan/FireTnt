package com.example.UserAuthenticationAndRoleManagement.Guest;

import com.example.BookingAndPayment.Booking.Booking;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestBookingViewDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestPropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.Host.HostService;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/guest")
public class GuestController {
    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);
    private final GuestService guestService;
    private final UserService userService;
    private final HostService hostService;

    public GuestController(GuestService guestService, UserService userService, HostService hostService) {
        this.guestService = guestService;
        this.userService = userService;
        this.hostService = hostService;
    }

    @GetMapping("/properties/{propertyId}/book")
    public String showBookingForm(@PathVariable Integer propertyId, Model model, Principal principal) {
        BookingDTO bookingForm = new BookingDTO();
        bookingForm.setPropertyId(propertyId);
        GuestPropertyDTO property = guestService.getPropertyById(propertyId);
        String guestId =hostService.getUidFromPrincipal(principal);
        String roleName = hostService.getRoleName();
        model.addAttribute("pricePerNight", property.getPricePerNight());
        model.addAttribute("property", property);
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("role", roleName);
        return "select-booking-dates"; // Thymeleaf template
    }
    @PostMapping("/properties/{propertyId}/book")
    public String bookProperty(@Valid @ModelAttribute("bookingForm") CreateBookingDTO dto,
                               BindingResult result,
                               @PathVariable Integer propertyId,
                               Model model,
                               Principal principal) {
        String guestId =hostService.getUidFromPrincipal(principal);
        dto.setGuestId(guestId);
        logger.info("Booking submitted: guestId={}, propertyId={}, checkIn={}, checkOut={}, totalPrice={}",
                dto.getGuestId(),
                dto.getPropertyId(),
                dto.getCheckIn(),
                dto.getCheckOut(),
                dto.getPrice());
        guestService.createBooking(dto);


        model.addAttribute("message", "Booking successful!");
        return "redirect:/guest/bookings"; // Redirect to MY RESERVATIOMS
    }
    @GetMapping("/bookings")
    public String showBookings(Model model, Principal principal) {
        String guestId = hostService.getUidFromPrincipal(principal);
        List<Booking> bookings = guestService.getBookingsByGuestId(guestId);

        List<GuestBookingViewDTO> viewDtos = bookings.stream()
                .map(booking -> {
                    GuestBookingViewDTO dto = new GuestBookingViewDTO();
                    dto.setBooking(booking);
                    GuestPropertyDTO property= guestService.getPropertyById(booking.getPropertyId());
                    dto.setPropertyTitle(property.getTitle());
                    dto.setPropertyDescription(property.getDescription());
                    return dto;
                }).toList();

        model.addAttribute("bookings", viewDtos);
        model.addAttribute("role", "GUEST");
        return "guest-reservations";
    }
    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Long bookingId, Principal principal) {
        guestService.cancelBooking(bookingId);
        return "redirect:/guest/bookings"; // Redirect to MY RESERVATIOMS
    }

}