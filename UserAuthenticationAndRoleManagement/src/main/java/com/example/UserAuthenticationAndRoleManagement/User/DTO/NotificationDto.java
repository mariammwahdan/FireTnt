package com.example.UserAuthenticationAndRoleManagement.User.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private Long recipientId;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;
    private boolean emailStatus;
}
