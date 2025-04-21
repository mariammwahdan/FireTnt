package com.example.UserAuthenticationAndRoleManagement.User.Client;

import com.example.UserAuthenticationAndRoleManagement.User.DTO.NotificationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class NotificationClient {
    private final WebClient client;

    public NotificationClient(WebClient notificationWebClient) {
        this.client = notificationWebClient;
    }

    public List<Long> fetchIdsByUser(Long userId) {
        return client.get()
                .uri("/api/notifications/ids/user/{id}", userId)
                .retrieve()
                .bodyToFlux(Long.class)
                .collectList()
                .block();
    }

    public NotificationDto fetchById(Long id) {
        return client.get()
                .uri("/api/notifications/{id}", id)
                .retrieve()
                .bodyToMono(NotificationDto.class)
                .block();
    }
}
