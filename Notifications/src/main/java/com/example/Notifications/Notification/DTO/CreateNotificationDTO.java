package com.example.Notifications.Notification.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Data
public class CreateNotificationDTO {
    @NotNull
    private Long recipientId;

    @NotNull
    private String recipientEmail;

    @NotNull
    private String message;

    public CreateNotificationDTO(Long recipientId, String recipientEmail, String message) {
        this.recipientId = recipientId;
        this.recipientEmail = recipientEmail;
        this.message = message;
    }

    public @NotNull Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(@NotNull Long recipientId) {
        this.recipientId = recipientId;
    }

    public @NotNull String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(@NotNull String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
