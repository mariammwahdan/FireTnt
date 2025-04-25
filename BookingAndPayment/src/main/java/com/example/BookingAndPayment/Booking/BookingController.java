package com.example.BookingAndPayment.Booking;

import com.example.BookingAndPayment.Annotations.RateLimit;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @RateLimit(limit = 3, duration = 60, keyPrefix = "createBooking")
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid CreateBookingDTO dto) {
        try {
            logger.info("Creating booking for Guest ID: {}", dto.getGuestId());
            Booking booking = bookingService.createBooking(dto);
            return ResponseEntity.ok(booking);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating booking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null); // Handle bad request error
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // Internal server error
        }
    }

    @GetMapping
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getAllBookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingById")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingsByUserId")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestId(userId));
    }

    @GetMapping("/property/{propertyId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingsByPropertyId")
    public ResponseEntity<List<Booking>> getBookingsByPropertyId(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getBookingsByPropertyId(propertyId));
    }

    @GetMapping("/profit/user/{userId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getTotalProfitByUserId")
    public ResponseEntity<Double> getTotalProfitByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getTotalProfitByUserId(userId));
    }

    @GetMapping("/profit/property/{propertyId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getTotalProfitByPropertyId")
    public ResponseEntity<Double> getTotalProfitByProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getTotalProfitByPropertyId(propertyId));
    }

    @GetMapping("/profit/total")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getTotalProfit")
    public ResponseEntity<Double> getTotalProfit() {
        return ResponseEntity.ok(bookingService.getTotalProfit());
    }

    @GetMapping("/{id}/isUpcoming")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "isUpcoming")
    public ResponseEntity<Boolean> isUpcoming(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.isUpcoming(id));
    }

    @GetMapping("/{id}/duration")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingDuration")
    public ResponseEntity<Integer> getBookingDuration(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingDuration(id));
    }

    @PostMapping("/{id}/cancel")
    @RateLimit(limit = 3, duration = 60, keyPrefix = "cancelBooking")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
