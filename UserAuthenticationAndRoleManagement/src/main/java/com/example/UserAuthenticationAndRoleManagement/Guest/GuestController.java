package com.example.UserAuthenticationAndRoleManagement.Guest;
import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.BookingAndPaymentClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.Client.NotificationsClient;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.*;
import com.example.UserAuthenticationAndRoleManagement.Host.HostService;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
import com.example.UserAuthenticationAndRoleManagement.User.User;
import com.example.UserAuthenticationAndRoleManagement.User.UserService;
import com.example.UserAuthenticationAndRoleManagement.auth.FirebasePrincipal;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final NotificationsClient notifClient;
    private final BookingAndPaymentClient bookingAndPaymentClient;




    public GuestController(GuestService guestService, UserService userService, HostService hostService, NotificationsClient notifClient, BookingAndPaymentClient bookingAndPaymentClient) {
        this.guestService = guestService;
        this.userService = userService;
        this.hostService = hostService;

        this.notifClient = notifClient;
        this.bookingAndPaymentClient = bookingAndPaymentClient;
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
    public String cancelBooking(
            @PathVariable Long bookingId,
            Principal principal
    ) {

        guestService.cancelBooking(bookingId);

        BookingDTO booking = guestService.getBookingById(bookingId);
        GuestPropertyDTO property = guestService.getPropertyById(booking.getPropertyId());

        Long guestUserId = userService.findUserIdByFirebaseUid(booking.getGuestId());
        String email = userService.fetchById(guestUserId).getEmail();

        String msg = String.format(
                "Your booking for property \"%s\" has been canceled.",
                property.getTitle()
        );
        CreateNotificationDTO notifDto = new CreateNotificationDTO(
                guestUserId,
                email,
                msg
        );

        try {
            notifClient.createNotification(notifDto);
        } catch (Exception e) {
            logger.error("Failed to send cancellation notification for booking {}: {}", bookingId, e.getMessage());
        }

        return "redirect:/guest/bookings";
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
    public String payForBooking(
            @PathVariable Long propertyId,
            @PathVariable Long bookingId,
            Model model
    ) {
        // Retrieve booking info to get the amount
        BookingDTO booking = bookingAndPaymentClient.getBookingById(bookingId);
        GuestPropertyDTO property = guestService.getPropertyById(propertyId);

        // Create PaymentDTO
        PaymentDTO payment = new PaymentDTO();
        payment.setBookingId(bookingId);
        payment.setAmount(booking.getPrice());
        payment.setStatus(PaymentDTO.PaymentStatus.COMPLETED);
        payment.setCreatedAt(LocalDateTime.now());

        // Call the service to persist the payment
        guestService.createPayment(payment);

        // After successful payment, send notification
        Long guestId = userService.findUserIdByFirebaseUid(booking.getGuestId());
        String email = userService.fetchById(guestId).getEmail();
        String msg = String.format(
                "Thank you for your payment for property %s! ",
                property.getTitle()
        );
        CreateNotificationDTO notifDto = new CreateNotificationDTO(guestId, email, msg);
        notifClient.createNotification(notifDto);

        // Redirect to reservations page
        return "redirect:/guest/bookings?paymentStatus=SUCCESS";
    }


    @GetMapping("/my-notifications")
    public String showNotifications(HttpSession session, Model model) {
        // 1. pull firebaseUid out of session
        String firebaseUid = (String) session.getAttribute("userFirebaseId");
        // 2. convert to our internal userId
        Long userId = userService.findUserIdByFirebaseUid(firebaseUid);
        // 3. call Notifications microservice
        List<NotificationDTO> notes = notifClient.fetchByUserId(userId);
        // 4. bind and render
        model.addAttribute("notifications", notes);
        model.addAttribute("role", "GUEST");
        return "myNotifications";  // Thymeleaf template under src/main/resources/templates/
    }
    @GetMapping("/reviews/create/{propertyId}")
    public String showCreateReviewForm(@PathVariable Long propertyId, Model model) {
        CreateReviewDTO reviewForm = new CreateReviewDTO();
        reviewForm.setPropertyId(propertyId);
        String roleName = hostService.getRoleName();
        model.addAttribute("role", roleName);
        model.addAttribute("reviewForm", reviewForm);
        return "create-review-form";
    }
    @PostMapping("/reviews/create/{propertyId}")
        public String createReview(@Valid @ModelAttribute("reviewForm") CreateReviewDTO dto,
                               BindingResult result,
                               @PathVariable Long propertyId,
                               Model model, Principal principal) {
        if (result.hasErrors()) {
            return "create-review-form";
        }
        String guestId = hostService.getUidFromPrincipal(principal);
        User user= userService.fetchByFirebaseUid(guestId);
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        dto.setGuestName(firstName + " " + lastName);
        dto.setGuestId(guestId);
        dto.setPropertyId(propertyId);
        logger.info("Review submitted: guestId={}, propertyId={}, rating={}, guest={}",
                dto.getGuestId(),
                dto.getPropertyId(),
                dto.getRating(),
                dto.getGuestName()
        );
        guestService.createReview(dto);
        model.addAttribute("message", "Review submitted successfully!");
        return "redirect:/api/users/properties/" + propertyId + "/details";
    }
}