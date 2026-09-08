package com.babakalizada.email.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String myEmail;

    @Async
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(myEmail);
        message.setTo(myEmail);
        message.setSubject("Spring Boot-dan ilk email 🚀");
        message.setText(
                "Salam Babək! Bu email Spring Boot tətbiqindən göndərildi."
        );

        mailSender.send(message);

        System.out.println("Email SMTP serverinə ötürüldü.");
    }
}