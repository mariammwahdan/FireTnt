package com.example.BookingAndPayment.Booking;
import com.example.BookingAndPayment.Annotations.DistributedLock;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import com.example.BookingAndPayment.Payment.PaymentRepository;
import com.example.BookingAndPayment.Payment.PaymentService;
import com.example.BookingAndPayment.Redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;

    private static final String ALL_BOOKINGS_CACHE_KEY = "bookings:all";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final String BOOKING_LOCK_PREFIX = "booking";
    private final PaymentService paymentService;
Logger logger = Logger.getLogger(BookingService.class.getName());
    @Autowired
    public BookingService(BookingRepository bookingRepository, PaymentRepository paymentRepository, RedisClient redisClient, ObjectMapper objectMapper, PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.redisClient = redisClient;
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
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
    public Double getTotalProfitByPropertyId(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId).stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.ACTIVE)
                .mapToDouble(Booking::getPrice).sum();
    }
    public List<Booking> getBookingsByGuestId(String guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getBookingsByPropertyId(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId);
    }
    public Double getTotalProfit() {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.ACTIVE)
                .mapToDouble(Booking::getPrice).sum();
    }
    public boolean isUpcoming(Long id) {
        Booking booking = getBookingById(id);
        return booking.getCheckIn().after(new Date());
    }

    public Double getTotalProfitByUserId(String userId) {
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
    public Long createBooking(CreateBookingDTO dto) {
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

        return savedBooking.getId();
    }

    public int getBookingDuration(Long id) {
        Booking booking = getBookingById(id);
        return booking.getNoOfNights();}

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
    public void deleteBooking(long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Property not found");
        }
        bookingRepository.deleteById(id);
        String propertyCacheKey = "property::" + id;
        try {
            redisClient.delete(propertyCacheKey); // Remove specific property cache
            redisClient.delete(ALL_BOOKINGS_CACHE_KEY); // Invalidate the list cache
            //log.info("[deleteProperty] Invalidated cache keys: {}, {}", propertyCacheKey, ALL_PROPERTIES_CACHE_KEY);
        } catch (Exception e) {
           // log.error("[deleteProperty] Error invalidating cache for keys {} and {}: {}", propertyCacheKey, ALL_PROPERTIES_CACHE_KEY, e.getMessage());
        }
    }
    public void deleteAllBookingsForProperty(long propertyId) {
        List<Booking> bookings = bookingRepository.findByPropertyId(propertyId);
        for (Booking booking : bookings) {
            bookingRepository.delete(booking);
            paymentService.deleteByBookingId(booking.getId());
            logger.info("Deleted booking with ID: " + booking.getId());
        }
        redisClient.delete(ALL_BOOKINGS_CACHE_KEY);
    }
}