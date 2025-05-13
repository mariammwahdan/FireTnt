package com.example.Notifications.Notification;
// HTML


import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/notifications/guest")
public class NotificationPageController {

    private final NotificationService svc;
    private final UserAuthClient   userAuth;

    public NotificationPageController(
            NotificationService svc,
            UserAuthClient userAuth
    ) {
        this.svc      = svc;
        this.userAuth = userAuth;
    }

    @GetMapping("/my-notifications")
    public String listPage(
            @RequestParam("firebaseUid") String firebaseUid,
            Model model
    ) {
        //  convert → userId
        Long userId = userAuth.fetchUserIdByFirebaseUid(firebaseUid);
        String email = userAuth.fetchEmailByUserId(userId);

        //  Check if welcome message already exists
        boolean hasWelcome = svc.getByUser(userId).stream()
                .anyMatch(n -> n.getMessage().startsWith("Welcome to FireTnt"));

        if (!hasWelcome) {
            svc.create(new CreateNotificationDTO(
                    userId,
                    email,
                    "Welcome to FireTnt, "  + "!"
            ));
        }



        //  load list
        model.addAttribute("notifications", svc.getByUser(userId));
        model.addAttribute("role", "GUEST");

        return "myNotifications";
    }

//    @GetMapping("/my-notifications")
//    public String listPage(
//            @RequestParam("firebaseUid") String firebaseUid,
//            Model model
//    ) {
//        // 1) Convert firebaseUid → our internal userId
//        Long userId = userAuth.fetchUserIdByFirebaseUid(firebaseUid);
//
//        // 2) Fetch the user's full profile so we can get their name
//        String userEmail = userAuth.fetchEmailByUserId(userId);
//
//
//        System.out.println(userEmail);
//        // 3) Build the exact welcome text
//        String welcomeMsg = "Welcome to FireTnt, " + userEmail + "!";
//
//
//        // 4) Load existing notifications for this user
//        List<Notification> notifications = svc.getByUser(userId);
//
//        System.out.println(notifications);
//
//        // 5) Only if *this* exact welcome isn't already there, create it
//        boolean hasExactWelcome = notifications.stream()
//                .anyMatch(n -> welcomeMsg.equals(n.getMessage()));
//
//        System.out.println(hasExactWelcome);
//
//        if (hasExactWelcome) {
//            svc.create(new CreateNotificationDTO(
//                    userId,
//                    userEmail,   // still store their email for reference
//                    welcomeMsg
//            ));
//            // reload so the new one appears immediately
//            notifications = svc.getByUser(userId);
//        }
//
//        // 6) Bind data to the template
//        model.addAttribute("notifications", notifications);
//        model.addAttribute("role",          "GUEST");
//
//        return "myNotifications";
//    }
}

