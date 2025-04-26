package com.example.Properties;

import com.example.Properties.Property.DTO.CreateReviewWithPropertyIdDTO;
import com.example.Properties.Review; // Create a simple Review model in Properties if you want
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public List<Review> getReviewsByPropertyId(Integer propertyId) {
        String url = "http://localhost:8081/api/reviews/property/" + propertyId;
        ResponseEntity<List<Review>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {}
        );
        return response.getBody();
    }

    public void createReview(Integer propertyId, CreateReviewWithPropertyIdDTO createReviewDTO) {
        String url =  "http://localhost:8081/api/reviews/"  + propertyId;

        restTemplate.postForEntity(url, createReviewDTO, Void.class);
    }


}
