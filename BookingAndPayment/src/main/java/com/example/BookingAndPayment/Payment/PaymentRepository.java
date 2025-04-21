package com.example.BookingAndPayment.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>
{
    // Custom Query to Find Payment by Booking ID
    Payment findByBookingId(long bookingId);

    // Custom Query to Find Payments by User ID
    List<Payment> findByUserId(long userId); // Ensure your Payment entity has a userId field

    // Custom Query to Find Payments by Property ID
    List<Payment> findByPropertyId(long propertyId);
}
