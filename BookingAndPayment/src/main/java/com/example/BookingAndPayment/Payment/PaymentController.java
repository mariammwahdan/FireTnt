package com.example.BookingAndPayment.Payment;

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
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) {
        Payment payment = paymentService.createPayment(createPaymentDTO);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    // Endpoint 2: Get All Payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Endpoint 3: Get Payment by Booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBookingId(@PathVariable long bookingId) {
        Payment payment = paymentService.getPaymentByBookingId(bookingId);
        if (payment != null) {
            return new ResponseEntity<>(payment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint 4: Get All Payments by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getAllPaymentsByUserId(@PathVariable long userId) {
        List<Payment> payments = paymentService.getAllPaymentsByUserId(userId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Endpoint 5: Get All Payments by Property ID
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Payment>> getAllPaymentsByPropertyId(@PathVariable long propertyId) {
        List<Payment> payments = paymentService.getAllPaymentsByPropertyId(propertyId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Endpoint 6: Update Payment Status
    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable long id, @RequestBody Payment.PaymentStatus status) {
        Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
        if (updatedPayment != null) {
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

