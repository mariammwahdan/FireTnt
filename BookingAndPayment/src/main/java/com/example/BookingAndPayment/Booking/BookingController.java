package com.example.BookingAndPayment.Booking;

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
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestId(userId));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Booking>> getBookingsByPropertyId(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getBookingsByPropertyId(propertyId));
    }

    @GetMapping("/profit/user/{userId}")
    public ResponseEntity<Double> getTotalProfitByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getTotalProfitByUserId(userId));
    }

    @GetMapping("/profit/property/{propertyId}")
    public ResponseEntity<Double> getTotalProfitByProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getTotalProfitByPropertyId(propertyId));
    }

    @GetMapping("/profit/total")
    public ResponseEntity<Double> getTotalProfit() {
        return ResponseEntity.ok(bookingService.getTotalProfit());
    }

    @GetMapping("/{id}/isUpcoming")
    public ResponseEntity<Boolean> isUpcoming(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.isUpcoming(id));
    }

    @GetMapping("/{id}/duration")
    public ResponseEntity<Integer> getBookingDuration(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingDuration(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
