package com.example.demo.services;

import com.example.demo.models.Alert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${demo.alertTo.mail}")
    private String alertToMail;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmailForAlert(Alert alert) throws JsonProcessingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vishwejeet.singh@iot83.com");
        message.setTo(alertToMail);
        message.setSubject("Alert notification for device %s".formatted(alert.getDeviceInfo().getDeviceId()));
        message.setText(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(alert));
        emailSender.send(message);
    }
}
