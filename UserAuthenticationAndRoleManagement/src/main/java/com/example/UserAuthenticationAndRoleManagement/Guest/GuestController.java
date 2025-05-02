package com.example.UserAuthenticationAndRoleManagement.Guest;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.BookingDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestBookingViewDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.GuestPropertyDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.PaymentDTO;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String showBookingForm(@PathVariable Long propertyId, Model model, Principal principal) {
        BookingDTO bookingForm = new BookingDTO();
        bookingForm.setPropertyId(propertyId);
        GuestPropertyDTO property = guestService.getPropertyById(propertyId);
        String guestId =hostService.getUidFromPrincipal(principal);
        String roleName = hostService.getRoleName();
        List<String> unavailableDates = guestService.getUnavailableDatesForProperty(propertyId);
        model.addAttribute("unavailableDates", unavailableDates);
        model.addAttribute("pricePerNight", property.getPricePerNight());
        model.addAttribute("property", property);
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("role", roleName);
        return "select-booking-dates"; // Thymeleaf template
    }
    @PostMapping("/properties/{propertyId}/book")
    public String bookProperty(@Valid @ModelAttribute("bookingForm") BookingDTO dto,
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
        Long bookingId= guestService.createBooking(dto);


        model.addAttribute("message", "Booking successful!");
        return "redirect:/guest/properties/" + propertyId + "/book/" + bookingId + "/pay";

    }
    @GetMapping("/bookings")
    public String showBookings(@RequestParam(required = false) String paymentStatus,
                               Model model, Principal principal) {
        String guestId = hostService.getUidFromPrincipal(principal);
        List<BookingDTO> bookings = guestService.getBookingsByGuestId(guestId);

        Map<Long, GuestPropertyDTO> propertyCache = new HashMap<>();

        List<GuestBookingViewDTO> viewDtos = bookings.stream()
                .map(booking -> {
                    GuestBookingViewDTO dto = new GuestBookingViewDTO();
                    dto.setBooking(booking);

                    Long propId = booking.getPropertyId();
                    GuestPropertyDTO property = propertyCache.computeIfAbsent(propId,
                            id -> guestService.getPropertyById(id));

                    dto.setPropertyTitle(property.getTitle());
                    dto.setPropertyDescription(property.getDescription());
                    dto.setPropertyLocation(property.getLocation());
                    dto.setPropertyType(property.getPropertyType());



                    return dto;
                }).toList();


        model.addAttribute("bookings", viewDtos);

        model.addAttribute("role", "GUEST");
        model.addAttribute("paymentStatus", paymentStatus);
        return "guest-reservations";
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Long bookingId, Principal principal) {
        guestService.cancelBooking(bookingId);
//guestService.refundPayment(bookingId);
        return "redirect:/guest/bookings"; // Redirect to MY RESERVATIOMS
    }

    @GetMapping("/properties/{propertyId}/book/{bookingId}/pay")
    public String showBookingConfirmation(@PathVariable Long bookingId, @PathVariable Long propertyId, Model model, Principal principal) {
        BookingDTO booking = guestService.getBookingById(bookingId);
        GuestPropertyDTO property = guestService.getPropertyById(propertyId);
        PaymentDTO payment = new PaymentDTO();
        payment.setBookingId(bookingId);
        payment.setAmount(booking.getPrice());
        payment.setStatus(PaymentDTO.PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        String roleName = hostService.getRoleName();
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("role", roleName);
        model.addAttribute("booking", booking);
        model.addAttribute("property", property);
        model.addAttribute("payment", payment);
        return "booking-confirmation";
    }

    @PostMapping("/properties/{propertyId}/book/{bookingId}/pay")
    public String payForBooking(@PathVariable Long propertyId,
                                @PathVariable Long bookingId,
                                Model model){

        // Retrieve booking info to get the amount
        BookingDTO booking = guestService.getBookingById(bookingId);

        // Create PaymentDTO
        PaymentDTO payment = new PaymentDTO();
        payment.setBookingId(bookingId);
        payment.setAmount(booking.getPrice());
        payment.setStatus(PaymentDTO.PaymentStatus.COMPLETED);
        payment.setCreatedAt(LocalDateTime.now());

        // Call the service to persist the payment
        guestService.createPayment(payment);

        // Redirect to reservations page
        return "redirect:/guest/bookings?paymentStatus=SUCCESS";

    }

}