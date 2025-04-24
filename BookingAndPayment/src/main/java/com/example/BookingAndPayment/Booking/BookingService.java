package com.example.BookingAndPayment.Booking;

import com.example.BookingAndPayment.Annotations.DistributedLock;
import com.example.BookingAndPayment.Booking.DTO.CreateBookingDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    @DistributedLock(
            keyPrefix = "booking:create",
            keyIdentifierExpression = "#dto.propertyId", // Lock per property to avoid double-booking
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

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
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
        return booking.getCheckIn().after(new Date());
    }

    public int getBookingDuration(Long id) {
        Booking booking = getBookingById(id);
        return booking.getNoOfNights();
    }

    @DistributedLock(
            keyPrefix = "booking:cancel",
            keyIdentifierExpression = "#id", // Lock by booking ID
            leaseTime = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
}
