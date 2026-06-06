package com.divyansh.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP — Divyansh Jaimini Website");
        message.setText(
            "Hello!\n\n" +
            "Your OTP for registration is:\n\n" +
            "  " + otp + "\n\n" +
            "This OTP is valid for 5 minutes.\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "— Divyansh Jaimini"
        );
        mailSender.send(message);
    }
}
