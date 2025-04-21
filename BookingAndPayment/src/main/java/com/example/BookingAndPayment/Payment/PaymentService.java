package com.example.BookingAndPayment.Payment;

import com.example.BookingAndPayment.Payment.DTO.CreatePaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // 1. Create Payment
    public Payment createPayment(CreatePaymentDTO createPaymentDTO) {
        Payment payment = new Payment();
        payment.setBookingId(createPaymentDTO.getBookingId());
        payment.setAmount(createPaymentDTO.getAmount());
        payment.setStatus(createPaymentDTO.getStatus());
        payment.setCreatedAt(createPaymentDTO.getCreatedAt());
        return paymentRepository.save(payment);
    }

    // 2. Get All Payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // 3. Get Payment by Booking ID
    public Payment getPaymentByBookingId(long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    // 4. Update Payment Status
    public Payment updatePaymentStatus(long id, Payment.PaymentStatus status) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus(status);
            return paymentRepository.save(payment);
        }
        return null;
    }
}
