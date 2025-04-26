package com.example.Properties;

import com.example.Properties.Review; // Create a simple Review model in Properties if you want
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ReviewsClient {

    private final RestTemplate restTemplate;

    @Autowired
    public ReviewsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String reviewsServiceUrl = "http://localhost:8081/api/reviews";

    public List<Review> getAllReviews() {
        Review[] reviewsArray = restTemplate.getForObject(reviewsServiceUrl, Review[].class);
        return Arrays.asList(reviewsArray);
    }
}
