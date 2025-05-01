package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BookingDTO {

    @Positive(message = "Property ID must be a positive number")
    private long propertyId;


    private String guestId;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-in date is required")
    private Date checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Check-out date is required")
    private Date checkOut;

    @Positive(message = "Price must be a positive number")
    private double price;

    @Min(value = 1, message = "Number of nights must be positive")
    private int noOfNights;
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.ACTIVE; // Default

    public enum BookingStatus {
        ACTIVE,
        CANCELLED
    }

    // Getters and Setters
    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public BookingStatus getStatus() {
        return status;
    }
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoOfNights() {
        return noOfNights;
    }

    public void setNoOfNights(int noOfNights) {
        this.noOfNights = noOfNights;
    }

    public boolean isDateRangeValid() {
        return checkIn != null && checkOut != null && checkOut.after(checkIn);
    }
}
