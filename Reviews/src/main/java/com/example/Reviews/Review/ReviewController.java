package com.example.Reviews.Review;

import com.example.Reviews.Review.DTO.CreateReviewDTO;
import com.example.Reviews.Review.DTO.CreateReviewWithPropertyIdDTO;
import com.example.Reviews.Review.DTO.UpdateReviewRatingDTO;
import com.example.Reviews.Review.DTO.UpdateReviewTextDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 1. Create Review
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody CreateReviewDTO dto) {
        Review createdReview = reviewService.createReview(dto);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Integer propertyId,
            @Valid @RequestBody CreateReviewWithPropertyIdDTO dto) {

        Review createdReview = reviewService.createReview(propertyId, dto);
        return ResponseEntity.ok(createdReview);
    }


    // 2. Get All Reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // Update only the review text
    @PutMapping("/{id}/text")
    public ResponseEntity<Review> updateReviewText(@PathVariable long id, @Valid @RequestBody UpdateReviewTextDTO dto) {
        Review updatedReview = reviewService.updateReviewText(id, dto);
        return ResponseEntity.ok(updatedReview);
    }

    // Update only the rating
    @PutMapping("/{id}/rating")
    public ResponseEntity<Review> updateReviewRating(@PathVariable long id, @Valid @RequestBody UpdateReviewRatingDTO dto) {
        Review updatedReview = reviewService.updateReviewRating(id, dto);
        return ResponseEntity.ok(updatedReview);
    }

    // Update only the review text by propertyId and reviewId
    @PutMapping("/property/{propertyId}/reviews/{id}/text")
    public ResponseEntity<Review> updateReviewTextByProperty(
            @PathVariable long propertyId,
            @PathVariable long id,
            @Valid @RequestBody UpdateReviewTextDTO dto) {
        Review updatedReview = reviewService.updateReviewTextByProperty(propertyId, id, dto);
        return ResponseEntity.ok(updatedReview);
    }

    // Update only the rating by propertyId and reviewId
    @PutMapping("/property/{propertyId}/reviews/{id}/rating")
    public ResponseEntity<Review> updateReviewRatingByProperty(
            @PathVariable long propertyId,
            @PathVariable long id,
            @Valid @RequestBody UpdateReviewRatingDTO dto) {
        Review updatedReview = reviewService.updateReviewRatingByProperty(propertyId, id, dto);
        return ResponseEntity.ok(updatedReview);
    }


    // 4. Get Reviews by Guest ID
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Review>> getReviewsByGuestId(@PathVariable long guestId) {
        List<Review> reviews = reviewService.getReviewsByGuestId(guestId);
        return ResponseEntity.ok(reviews);
    }

    // 5. Get Reviews by Property ID
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsByPropertyId(@PathVariable long propertyId) {
        List<Review> reviews = reviewService.getReviewsByPropertyId(propertyId);
        return ResponseEntity.ok(reviews);
    }

    // 6. Get Reviews by Guest ID and Property ID
    @GetMapping("/filter")
    public ResponseEntity<List<Review>> getReviewsByGuestAndProperty(
            @RequestParam long guestId,
            @RequestParam long propertyId
    ) {
        List<Review> reviews = reviewService.getReviewsByGuestAndProperty(guestId, propertyId);
        return ResponseEntity.ok(reviews);
    }

    // 7. Delete Review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // 8. Get Average Rating by Property ID
    @GetMapping("/property/{propertyId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByProperty(@PathVariable long propertyId) {
        double avgRating = reviewService.getAverageRatingByPropertyId(propertyId);
        return ResponseEntity.ok(avgRating);
    }
}

