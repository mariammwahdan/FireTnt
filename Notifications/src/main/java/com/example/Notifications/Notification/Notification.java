package com.example.Notifications.Notification;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId;
    private String recipientEmail;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;
    private boolean emailStatus;

    @PrePersist
    void initDefaults() {
        timestamp   = LocalDateTime.now();
        isRead      = false;
        emailStatus = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public boolean sendMail() {
        // integrate your mail sender here
        this.emailStatus = true;
        return true;
    }

    public void resendMail() {
        sendMail();
    }
}
