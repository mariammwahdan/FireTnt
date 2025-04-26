package com.example.Reviews.Review;

import com.example.Reviews.Review.DTO.CreateReviewDTO;
import com.example.Reviews.Review.DTO.UpdateReviewRatingDTO;
import com.example.Reviews.Review.DTO.UpdateReviewTextDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(CreateReviewDTO dto) {
        Review review = new Review(dto.getGuestId(), dto.getPropertyId(), dto.getReviewText(), dto.getRating());
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review updateReviewText(long id, UpdateReviewTextDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setReviewText(dto.getReviewText());
        return reviewRepository.save(review);
    }

    public Review updateReviewRating(long id, UpdateReviewRatingDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setRating(dto.getRating());
        return reviewRepository.save(review);
    }


    public void deleteReview(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }

    public List<Review> getReviewsByGuestId(long guestId) {
        return reviewRepository.findByGuestId(guestId);
    }

    public List<Review> getReviewsByPropertyId(long propertyId) {
        return reviewRepository.findByPropertyId(propertyId);
    }

    public List<Review> getReviewsByGuestAndProperty(long guestId, long propertyId) {
        return reviewRepository.findByGuestIdAndPropertyId(guestId, propertyId);
    }

    public double getAverageRatingByPropertyId(long propertyId) {
        List<Review> reviews = reviewRepository.findByPropertyId(propertyId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double sum = reviews.stream().mapToDouble(Review::getRating).sum();
        return sum / reviews.size();
    }
}
