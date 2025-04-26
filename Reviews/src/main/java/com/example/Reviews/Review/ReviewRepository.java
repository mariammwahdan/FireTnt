package com.example.Reviews.Review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGuestId(long guestId);
    List<Review> findByPropertyId(long propertyId);
    List<Review> findByGuestIdAndPropertyId(long guestId, long propertyId);
}
