package com.example.BookingAndPayment.Payment;

import com.example.BookingAndPayment.Annotations.RateLimit;
import com.example.BookingAndPayment.Payment.DTO.CreatePaymentDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Endpoint 1: Create Payment
    @PostMapping("/create")
    @RateLimit(limit = 3, duration = 60, keyPrefix = "createPayment")
    public ResponseEntity<String> createPayment(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) {
  paymentService.createPayment(createPaymentDTO);
        return ResponseEntity.ok( "Payment created successfully");
    }

    // Endpoint 2: Get All Payments
    @GetMapping
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getAllPayments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Endpoint 3: Get Payment by Booking ID
    @GetMapping("/booking/{bookingId}")
    @RateLimit(limit = 80, duration = 60, keyPrefix = "getPaymentByBookingId")
    public ResponseEntity<Payment> getPaymentByBookingId(@PathVariable long bookingId) {
        Payment payment = paymentService.getPaymentByBookingId(bookingId);
        if (payment != null) {
            return new ResponseEntity<>(payment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Endpoint 4: Update Payment Status
    @PutMapping("/{id}/status")
    @RateLimit(limit = 3, duration = 60, keyPrefix = "updatePaymentStatus")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable long id, @RequestBody Payment.PaymentStatus status) {
        Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
        if (updatedPayment != null) {
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
