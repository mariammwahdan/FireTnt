package com.example.UserAuthenticationAndRoleManagement.Guest.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private Long recipientId;
    private String recipientEmail;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;

    // getters + setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp ; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
