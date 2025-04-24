package com.example.Notifications.Notification.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNotificationDTO {
    @NotNull
    private Long recipientId;

    @NotNull
    private String recipientEmail;

    @NotNull
    private String message;
}
