package com.example.UserAuthenticationAndRoleManagement.Guest.Client;


import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import com.example.UserAuthenticationAndRoleManagement.Guest.DTO.NotificationDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationsClient {
    private final RestTemplate rest;
    private final String base = "http://localhost:8083/api/notifications";

    public NotificationsClient(RestTemplateBuilder b) {
        this.rest = b.build();
    }

    public List<NotificationDTO> fetchByUserId(Long userId) {
        NotificationDTO[] arr = rest.getForObject(
                base + "/my-notifications/json?userId={userId}",
                NotificationDTO[].class,
                userId
        );
        if (arr == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(arr));
    }

    public NotificationDTO createNotification(CreateNotificationDTO dto) {
        return rest.postForObject(
                base + "/send-welcome-message/json",
                dto,
                NotificationDTO.class
        );
    }
}

