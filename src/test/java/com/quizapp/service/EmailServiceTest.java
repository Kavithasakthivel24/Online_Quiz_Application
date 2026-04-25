package com.quizapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    // ✅ 1. Registration Email Test
    @Test
    void testSendRegistrationEmail() {

        String email = "test@gmail.com";
        String username = "admin";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendRegistrationEmail(email, username);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    // ✅ 2. OTP Email Test
    @Test
    void testSendOtpEmail() {

        String email = "test@gmail.com";
        String otp = "123456";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendOtpEmail(email, otp);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}