package com.project.bodify.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Password Reset Request";
        //String url = "http://localhost:9092/reset-password?token=" + token;
        String serverAddress = getServerAddress(); // Get server address dynamically
        String url = "http://" + serverAddress + ":9092/reset-password?token=" + token;
        String message = "Click the link to reset your password: " + url;

        sendEmail(to, subject, message);
    }

    private void sendEmail(String to, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    
    private String getServerAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress(); // Returns the IP address as a string
        } catch (UnknownHostException e) {
            e.printStackTrace(); // Handle exception appropriately
            return "localhost"; // Fallback option if address retrieval fails
        }}
}
