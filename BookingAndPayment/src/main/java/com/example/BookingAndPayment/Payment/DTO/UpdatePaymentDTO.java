package com.example.BookingAndPayment.Payment.DTO;

import com.example.BookingAndPayment.Payment.Payment;
import jakarta.validation.constraints.*;

public class UpdatePaymentDTO {

    @Positive(message = "Payment ID must be a positive number")
    private long id;

    @Positive(message = "Amount must be a positive number")
    private double amount;

    @NotNull(message = "Payment status is required")
    private Payment.PaymentStatus status;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Payment.PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(Payment.PaymentStatus status) {
        this.status = status;
    }

    // Custom validation to check if the status is allowed for updates
    public boolean isStatusValid() {
        return status != null && (status == Payment.PaymentStatus.PENDING || status == Payment.PaymentStatus.FAILED);
    }
}
