package com.refresh.email.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
	String generateVerificationCode();
    void sendVerificationEmail(String toEmail, String verificationCode);
}
