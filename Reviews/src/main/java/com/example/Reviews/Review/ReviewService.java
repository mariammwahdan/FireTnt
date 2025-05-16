package com.example.Reviews.Review;

import com.example.Reviews.Annotations.DistributedLock;
import com.example.Reviews.Review.DTO.*;
import com.example.Reviews.Redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final String ALL_REVIEWS_CACHE_KEY = "reviews:all";
    private static final String PROPERTY_REVIEWS_PREFIX = "reviews:property:";
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RedisClient redisClient, ObjectMapper objectMapper) {
        this.reviewRepository = reviewRepository;
        this.redisClient = redisClient;
        this.objectMapper = objectMapper;
    }

    @DistributedLock(keyPrefix = "review:create", keyIdentifierExpression = "#dto.guestId + ':' + #dto.propertyId", leaseTime = 30, timeUnit = TimeUnit.SECONDS)
    public Review createReview(CreateReviewDTO dto) {
        Review review = new Review(dto.getGuestName(), dto.getGuestId(), dto.getPropertyId(), dto.getReviewText(), dto.getRating());
        Review saved = reviewRepository.save(review);
        invalidateCaches(dto.getPropertyId());
        return saved;
    }

    @DistributedLock(keyPrefix = "review:create", keyIdentifierExpression = "#propertyId + ':' + #dto.guestId", leaseTime = 30, timeUnit = TimeUnit.SECONDS)
    public Review createReview(Integer propertyId, CreateReviewDTO dto) {
        Review review = new Review(dto.getGuestName(), dto.getGuestId(), propertyId, dto.getReviewText(), dto.getRating());
        logger.info( "Creating review for Guest: " + dto.getGuestName());
        Review saved = reviewRepository.save(review);
        logger.info( "Creating review for Guest: " + saved.getGuestName());
        invalidateCaches(propertyId);
        return saved;
    }

    public List<Review> getAllReviews() {
        try {
            String cached = redisClient.get(ALL_REVIEWS_CACHE_KEY);
            if (cached != null) {
                System.out.println("Cache hit for all reviews."); // Cache hit message
                return objectMapper.readValue(cached,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Review.class));
            } else {
                System.out.println("Cache miss for all reviews."); // Cache miss message
            }
        } catch (JsonProcessingException e) {
            logger.warning("Failed to deserialize all reviews: " + e.getMessage());
        }

        List<Review> reviews = reviewRepository.findAll();
        try {
            redisClient.set(ALL_REVIEWS_CACHE_KEY, objectMapper.writeValueAsString(reviews), CACHE_TTL);
            System.out.println("Cache updated with all reviews."); // Cache update message
        } catch (JsonProcessingException e) {
            logger.warning("Failed to cache all reviews: " + e.getMessage());
        }
        return reviews;
    }


    public List<Review> getReviewsByPropertyId(long propertyId) {
        String key = PROPERTY_REVIEWS_PREFIX + propertyId;
        try {
            String cached = redisClient.get(key);
            if (cached != null) {
                return objectMapper.readValue(cached,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Review.class));
            }
        } catch (JsonProcessingException e) {
            logger.warning("Failed to deserialize reviews for property " + propertyId + ": " + e.getMessage());
        }
        List<Review> reviews = reviewRepository.findByPropertyId(propertyId);
          try {
            redisClient.set(key, objectMapper.writeValueAsString(reviews), CACHE_TTL);
        } catch (JsonProcessingException e) {
            logger.warning("Failed to cache reviews for property " + propertyId + ": " + e.getMessage());
        }
        return reviews;
    }

    @DistributedLock(keyPrefix = "review:updateText", keyIdentifierExpression = "#id", leaseTime = 15, timeUnit = TimeUnit.SECONDS)
    public Review updateReviewText(long id, UpdateReviewTextDTO dto) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setReviewText(dto.getReviewText());
        Review updated = reviewRepository.save(review);
        invalidateCaches(review.getPropertyId());
        return updated;
    }

    @DistributedLock(keyPrefix = "review:updateRating", keyIdentifierExpression = "#id", leaseTime = 15, timeUnit = TimeUnit.SECONDS)
    public Review updateReviewRating(long id, UpdateReviewRatingDTO dto) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setRating(dto.getRating());
        Review updated = reviewRepository.save(review);
        invalidateCaches(review.getPropertyId());
        return updated;
    }

    @DistributedLock(keyPrefix = "review:updateTextByProperty", keyIdentifierExpression = "#propertyId + ':' + #reviewId", leaseTime = 15, timeUnit = TimeUnit.SECONDS)
    public Review updateReviewTextByProperty(long propertyId, long reviewId, UpdateReviewTextDTO dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        if (review.getPropertyId() != propertyId) throw new RuntimeException("Review does not belong to the specified property");
        review.setReviewText(dto.getReviewText());
        Review updated = reviewRepository.save(review);
        invalidateCaches(propertyId);
        return updated;
    }

    @DistributedLock(keyPrefix = "review:updateRatingByProperty", keyIdentifierExpression = "#propertyId + ':' + #reviewId", leaseTime = 15, timeUnit = TimeUnit.SECONDS)
    public Review updateReviewRatingByProperty(long propertyId, long reviewId, UpdateReviewRatingDTO dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        if (review.getPropertyId() != propertyId) throw new RuntimeException("Review does not belong to the specified property");
        review.setRating(dto.getRating());
        Review updated = reviewRepository.save(review);
        invalidateCaches(propertyId);
        return updated;
    }

    @DistributedLock(keyPrefix = "review:delete", keyIdentifierExpression = "#id", leaseTime = 20, timeUnit = TimeUnit.SECONDS)
    public void deleteReview(long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
        invalidateCaches(review.getPropertyId());
    }

    public List<Review> getReviewsByGuestId(String guestId) {
        return reviewRepository.findByGuestId(guestId);
    }

    public List<Review> getReviewsByGuestAndProperty(String guestId, long propertyId) {
        return reviewRepository.findByGuestIdAndPropertyId(guestId, propertyId);
    }

    public double getAverageRatingByPropertyId(long propertyId) {
        List<Review> reviews = getReviewsByPropertyId(propertyId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }

    private void invalidateCaches(long propertyId) {
        redisClient.delete(ALL_REVIEWS_CACHE_KEY);
        redisClient.delete(PROPERTY_REVIEWS_PREFIX + propertyId);
    }
}
