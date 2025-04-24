package com.example.Notifications.Notification;

import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService svc;

    public NotificationController(NotificationService svc) {
        this.svc = svc;
    }

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

    @PutMapping("/{id}/resend")
    public Notification resendMail(@PathVariable Long id) {
        return svc.resendMail(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}