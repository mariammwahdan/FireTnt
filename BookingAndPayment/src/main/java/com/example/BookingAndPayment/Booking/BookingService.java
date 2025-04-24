package com.example.BookingAndPayment.Booking;

import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.restTemplate = new RestTemplate(); // Use Spring-managed bean if needed
    }

    public Booking createBooking(CreateBookingDTO dto) {
        // Validate the date range (Check-out must be after Check-in)
        if (!dto.isDateRangeValid()) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        // Validate if the Guest exists via Guest microservice
        String guestServiceUrl = "http://localhost:8081/api/guests/" + dto.getGuestId();
        try {
            // Making a GET request to check if guest exists
            restTemplate.getForObject(guestServiceUrl, Object.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Guest ID " + dto.getGuestId() + " does not exist", e);
        }

        // Create new Booking entity and set values from CreateBookingDTO
        Booking booking = new Booking();
        booking.setPropertyId(dto.getPropertyId());
        booking.setGuestId(dto.getGuestId());
        booking.setCheckIn(dto.getCheckIn());
        booking.setCheckOut(dto.getCheckOut());
        booking.setPrice(dto.getPrice());
        booking.setNoOfNights(dto.getNoOfNights());
        booking.setStatus(Booking.BookingStatus.ACTIVE);  // Set initial status as ACTIVE

        // Save the booking into the database using BookingRepository
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getBookingsByGuestId(Long guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getBookingsByPropertyId(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId);
    }

    public Double getTotalProfitByUserId(Long userId) {
        return bookingRepository.findByGuestId(userId).stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.ACTIVE)
                .mapToDouble(Booking::getPrice).sum();
    }

    public Double getTotalProfitByPropertyId(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId).stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.ACTIVE)
                .mapToDouble(Booking::getPrice).sum();
    }

    public Double getTotalProfit() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.ACTIVE)
                .mapToDouble(Booking::getPrice).sum();
    }

    public boolean isUpcoming(Long id) {
        Booking booking = getBookingById(id);
        return booking.getCheckIn().after(new Date()); // Compare with current date
    }

    public int getBookingDuration(Long id) {
        Booking booking = getBookingById(id);
        return booking.getNoOfNights();
    }

    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED); // Update the status to CANCELLED
        return bookingRepository.save(booking); // Save the updated status
    }
}
