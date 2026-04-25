package com.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String username) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kavithasakthivel5420@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Registration Successful");

        message.setText("Hi " + username + ",\n\n" +
                "Your registration is successful.\n" +
                "Welcome to Quiz App 🎉");

        mailSender.send(message);
    }
    
    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");

        message.setText("Your OTP for password reset is: " + otp);

        mailSender.send(message);
    }
}