package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import com.example.BookingAndPayment.Booking.Booking;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class BookingDTO {

    private long propertyId;
    private String guestId;
    private Date checkIn;
    private Date checkOut;

    private double price;

    private int noOfNights;
    private Booking.BookingStatus status = Booking.BookingStatus.ACTIVE;
//
//    // Getters and Setters
//    public long getPropertyId() {
//        return propertyId;
//    }
//
//    public void setPropertyId(long propertyId) {
//        this.propertyId = propertyId;
//    }
//
//    public String getGuestId() {
//        return guestId;
//    }
//
//    public void setGuestId(String guestId) {
//        this.guestId = guestId;
//    }
//
//    public Date getCheckIn() {
//        return checkIn;
//    }
//
//    public void setCheckIn(Date checkIn) {
//        this.checkIn = checkIn;
//    }
//
//    public Date getCheckOut() {
//        return checkOut;
//    }
//
//    public void setCheckOut(Date checkOut) {
//        this.checkOut = checkOut;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public int getNoOfNights() {
//        return noOfNights;
//    }
//
//    public void setNoOfNights(int noOfNights) {
//        this.noOfNights = noOfNights;
//    }
//
//    public boolean isDateRangeValid() {
//        return checkIn != null && checkOut != null && checkOut.after(checkIn);
//    }
}
