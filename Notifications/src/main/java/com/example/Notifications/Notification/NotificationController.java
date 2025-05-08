package com.example.Notifications.Notification;


import com.example.Notifications.Notification.DTO.CreateNotificationDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




import java.util.List;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService svc;
    private final UserAuthClient userAuthClient;



    public NotificationController(NotificationService svc, UserAuthClient userAuthClient) {
        this.svc = svc;
        this.userAuthClient = userAuthClient;
    }

    @PostMapping("/send-welcome-message/json")
    @ResponseBody
    public ResponseEntity<Notification> createNotification(@RequestBody CreateNotificationDTO dto) {
        // Create the notification and send the email
        Notification notification = svc.create(dto);

        // Return a response with the created notification
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping("/my-notifications/json")
    @ResponseBody
    public List<Notification> getMyNotifications(@RequestParam Long userId) {
        return svc.getByUser(userId);
    }

    @PostMapping("/send-welcome-message")
    public String createNotification(@RequestBody CreateNotificationDTO dto, Model model) {
        // Create the notification
        Notification notification = svc.create(dto);

        // Add the notification object to the model to display in the HTML view
        model.addAttribute("notification", notification);

        return "notificationConfirmation";
    }

    // Endpoint to fetch and display user notifications in HTML



    @GetMapping("/user/{userId}")
    public List<Notification> getByUser(@PathVariable Long userId) {
        return svc.getByUser(userId);
    }

    //another get request but returning HTML file not json like above
    //return string "showNotification"

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