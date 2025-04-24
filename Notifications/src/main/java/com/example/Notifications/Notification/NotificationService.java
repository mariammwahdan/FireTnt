package com.example.Notifications.Notification;


import com.example.Notifications.Notification.DTO.CreateNotificationDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
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
    public Notification create(CreateNotificationDTO dto) {
        Notification n = new Notification();
        n.setRecipientId(dto.getRecipientId());
        n.setRecipientEmail(dto.getRecipientEmail());
        n.setMessage(dto.getMessage());
        Notification saved = notificationRepository.save(n);

        sendMailInternal(saved);

        return saved;
    }

    private void sendMailInternal(Notification n) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, false, "UTF-8");
            helper.setTo(n.getRecipientEmail());
            helper.setSubject("Notification #" + n.getId());
            helper.setText(n.getMessage(), false);

            mailSender.send(mime);

            n.setEmailStatus(true);
            notificationRepository.save(n);

        } catch (MessagingException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Email send failed", ex
            );
        }
    }


    @Transactional
    public Notification markAsRead(Long id) {
        Notification n = getById(id);
        n.markAsRead();
        return n;
    }

//    @Transactional
//    public boolean sendMail(Long id) {
//        Notification n = getById(id);
//        return n.sendMail();
//    }

    @Transactional
    public Notification resendMail(Long id) {
        Notification n = getById(id);
        sendMailInternal(n);
        return n;
    }

    @Transactional
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}
