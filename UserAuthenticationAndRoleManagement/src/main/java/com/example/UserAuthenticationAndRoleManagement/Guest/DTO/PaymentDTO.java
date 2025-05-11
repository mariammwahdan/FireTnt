package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class PaymentDTO {

    @Positive(message = "Booking ID must be a positive number")
    private long bookingId;

    @Positive(message = "Amount must be a positive number")
    private double amount;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
    public enum PaymentStatus {
        REFUNDED,
        PENDING,
        COMPLETED,
        FAILED
    }
    @NotNull(message = "Created date is required")
    private LocalDateTime createdAt; // Validating that createdAt is not null

    // Getters and Setters
    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Custom validation to ensure createdAt is not in the future
    public boolean isCreatedAtValid() {
        return createdAt != null && createdAt.isBefore(LocalDateTime.now());
    }

}
