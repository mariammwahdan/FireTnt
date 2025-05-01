package com.example.BookingAndPayment.Booking;

import com.example.BookingAndPayment.Annotations.RateLimit;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "createBooking")
    public ResponseEntity<String> createBooking(@RequestBody @Valid CreateBookingDTO dto) {
        bookingService.createBooking(dto);
        return ResponseEntity.ok( "Booking created successfully");
    }
    @GetMapping("/property/{propertyId}/booked-dates")
    public ResponseEntity<List<String>> getBookedDates(@PathVariable Long propertyId) {
        List<Booking> bookings = bookingService.getBookingsByPropertyId(propertyId);

        List<String> bookedDates = bookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.ACTIVE)
                .flatMap(b -> {
                    List<String> range = new java.util.ArrayList<>();
                    java.util.Calendar date = java.util.Calendar.getInstance();
                    date.setTime(b.getCheckIn());
                    while (!date.getTime().after(b.getCheckOut())) {
                        range.add(new java.text.SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));
                        date.add(java.util.Calendar.DATE, 1);
                    }
                    return range.stream();
                })
                .distinct()
                .toList();

        return ResponseEntity.ok(bookedDates);
    }


    @GetMapping
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getAllBookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id:[0-9]+}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingById")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingsByUserId")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestId(userId));
    }

    @GetMapping("/property/{propertyId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getBookingsByPropertyId")
    public ResponseEntity<List<Booking>> getBookingsByPropertyId(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getBookingsByPropertyId(propertyId));
    }

    @GetMapping("/profit/user/{userId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getTotalProfitByUserId")
    public ResponseEntity<Double> getTotalProfitByUser(@PathVariable String userId) {
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
    @RateLimit(limit = 80, duration = 60, keyPrefix = "cancelBooking")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}