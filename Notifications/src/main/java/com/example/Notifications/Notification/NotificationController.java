package com.example.Notifications.Notification;

import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService svc;

    public NotificationController(NotificationService svc) {
        this.svc = svc;
    }

    @PostMapping("/send-welcome-message")
    public ResponseEntity<Notification> createNotification(@RequestBody CreateNotificationDTO dto) {
        // Create the notification and send the email
        Notification notification = svc.create(dto);

        // Return a response with the created notification
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

//    @PostMapping("/send-welcome-email")
//    public ResponseEntity<String> sendWelcomeEmail(@RequestBody Map<String, Object> notificationRequest) {
//        // Extract recipientEmail and message from the request body
//        String recipientEmail = (String) notificationRequest.get("recipientEmail");
//        String message = (String) notificationRequest.get("message");
//
//        // Create a DTO to pass to the NotificationService
//        CreateNotificationDTO dto = new CreateNotificationDTO();
//        dto.setRecipientEmail(recipientEmail);
//        dto.setMessage(message);
//
//        // Call the NotificationService to send the email
//        svc.create(dto);  // Pass DTO instead of the entity
//
//        return ResponseEntity.ok("Email sent successfully.");
//    }



    @GetMapping("/user/{userId}")
    public List<Notification> getByUser(@PathVariable Long userId) {
        return svc.getByUser(userId);
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable Long id) {
        return svc.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestBody CreateNotificationDTO dto) {
        return svc.create(dto);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        return svc.markAsRead(id);
    }

//    @PutMapping("/{id}/send")
//    public boolean sendMail(@PathVariable Long id) {
//        return svc.sendMail(id);
//    }

//    @PutMapping("/{id}/resend")
//    public Notification resendMail(@PathVariable Long id) {
//        return svc.resendMail(id);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}