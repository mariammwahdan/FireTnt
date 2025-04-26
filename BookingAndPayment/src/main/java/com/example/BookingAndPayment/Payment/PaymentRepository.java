package com.example.BookingAndPayment.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>
{
    // Custom Query to Find Payment by Booking ID
    Payment findByBookingId(long bookingId);

}