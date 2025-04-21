package com.example.Notifications.Notification;


import com.example.Notifications.Notification.DTO.CreateNotificationDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification getById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Not found"));
    }

    public List<Notification> getByUser(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    @Transactional
    public Notification create(Notification notification) {

        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification markAsRead(Long id) {
        Notification n = getById(id);
        n.markAsRead();
        return n;
    }

    @Transactional
    public boolean sendMail(Long id) {
        Notification n = getById(id);
        return n.sendMail();
    }

    @Transactional
    public Notification resendMail(Long id) {
        Notification n = getById(id);
        n.resendMail();
        return n;
    }

    @Transactional
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}
