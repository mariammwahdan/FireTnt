package com.example.BookingAndPayment.Booking;
import com.example.BookingAndPayment.Annotations.DistributedLock;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import com.example.BookingAndPayment.Redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;

    private static final String ALL_BOOKINGS_CACHE_KEY = "bookings:all";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final String BOOKING_LOCK_PREFIX = "booking";

    @Autowired
    public BookingService(BookingRepository bookingRepository, RedisClient redisClient, ObjectMapper objectMapper) {
        this.bookingRepository = bookingRepository;
        this.redisClient = redisClient;
        this.objectMapper = objectMapper;
    }

    public List<Booking> getAllBookings() {
        try {
            String cachedBookings = redisClient.get(ALL_BOOKINGS_CACHE_KEY);
            if (cachedBookings != null) {
                return objectMapper.readValue(cachedBookings, List.class);
            }
        } catch (JsonProcessingException e) {
            // Fallback to DB if cache retrieval fails
        }

        List<Booking> bookings = bookingRepository.findAll();
        try {
            String bookingsJson = objectMapper.writeValueAsString(bookings);
            redisClient.set(ALL_BOOKINGS_CACHE_KEY, bookingsJson, CACHE_TTL);
        } catch (JsonProcessingException e) {
            // Cache store failed, no problem
        }

        return bookings;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
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

    @DistributedLock(
            keyPrefix = BOOKING_LOCK_PREFIX,
            keyIdentifierExpression = "#dto.propertyId",
            leaseTime = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public Booking createBooking(CreateBookingDTO dto) {
        if (!dto.isDateRangeValid()) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        Booking booking = new Booking();
        booking.setPropertyId(dto.getPropertyId());
        booking.setGuestId(dto.getGuestId());
        booking.setCheckIn(dto.getCheckIn());
        booking.setCheckOut(dto.getCheckOut());
        booking.setPrice(dto.getPrice());
        booking.setNoOfNights(dto.getNoOfNights());
        booking.setStatus(Booking.BookingStatus.ACTIVE);

        Booking savedBooking = bookingRepository.save(booking);

        // 🧹 Invalidate cache after create
        redisClient.delete(ALL_BOOKINGS_CACHE_KEY);

        return savedBooking;
    }

    @DistributedLock(
            keyPrefix = BOOKING_LOCK_PREFIX,
            keyIdentifierExpression = "#id",
            leaseTime = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);

        // 🧹 Invalidate cache after update
        redisClient.delete(ALL_BOOKINGS_CACHE_KEY);

        return updatedBooking;
    }
}
