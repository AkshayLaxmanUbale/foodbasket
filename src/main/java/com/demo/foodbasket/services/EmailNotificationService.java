package com.demo.foodbasket.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}") 
    private String sender;

    public void sendEmailVerificationMail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject("Verify Email!!");

        String body = "Please click on below link to verify your email.\n" +
        "http://localhost:8080/auth/user/verify/%s";

        message.setText(String.format(body, to));
        mailSender.send(message);
    }
}
