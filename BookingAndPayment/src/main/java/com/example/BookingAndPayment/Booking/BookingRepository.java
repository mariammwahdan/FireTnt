package com.example.BookingAndPayment.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByGuestId(long guestId);
    boolean existsByPropertyId(long propertyId);
    List<Booking> findByGuestId(Long guestId);
    List<Booking> findByPropertyId(Long propertyId);
}
