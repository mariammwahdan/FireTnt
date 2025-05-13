package com.example.Notifications.Notification;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAuthClient {
    private final RestTemplate tpl;

    public UserAuthClient(RestTemplateBuilder builder) {
        this.tpl = builder.build();
    }
    public Long fetchUserIdByFirebaseUid(String uid) {

        return tpl.getForObject(
                "http://localhost:8080/api/users/by-firebase-uid?uid={uid}",
                Long.class,
                uid
        );
    }

    public String fetchEmailByUserId(Long userId) {
        return tpl.getForObject(
                "http://localhost:8080/api/users/{id}/email",
                String.class,
                userId
        );
    }
}
