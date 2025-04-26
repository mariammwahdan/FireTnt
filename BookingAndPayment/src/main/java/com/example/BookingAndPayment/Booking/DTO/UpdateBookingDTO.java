package com.example.BookingAndPayment.Booking.DTO;

import jakarta.validation.constraints.*;
import java.util.Date;

public class UpdateBookingDTO {

    @Positive(message = "Property ID must be a positive number")
    private long propertyId;

    @Positive(message = "Guest ID must be a positive number")
    private long guestId;

    @NotNull(message = "Check-in date is required")
    private Date checkIn;

    @NotNull(message = "Check-out date is required")
    private Date checkOut;

    @Positive(message = "Price must be a positive number")
    private double price;

    @Min(value = 1, message = "Number of nights must be positive")
    private int noOfNights;

    // Getters and Setters
    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
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