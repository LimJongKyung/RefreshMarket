package com.refresh.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private String generatedCode;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 확인 코드 생성 메서드
    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        int verificationCode = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성
        generatedCode = String.valueOf(verificationCode); // 생성된 코드 저장
        return generatedCode;
    }

    // 확인 이메일 발송 메서드
    @Override
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Verification Code");
        message.setText("안녕하세요! RefreshMarket입니다. 당신의 인증 번호는: " + verificationCode + "입니다.");
        mailSender.send(message);
    }
    
    // 인증 코드 검증 메서드
    public boolean validateVerificationCode(String userInputCode) {
        return generatedCode != null && generatedCode.equals(userInputCode);
    }

    // 이메일로 ID 전송 메서드
    public void sendUserIdToEmail(String toEmail, String userId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your User ID");
        message.setText("안녕하세요! RefreshMarket입니다. 당신의 ID는: " + userId + "입니다.");
        mailSender.send(message);
    }
    
    // 이메일로 Passwd 전송 메서드
    public void sendUserPasswdToEmail(String toEmail, String userPasswd) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your User Password");
        message.setText("안녕하세요! RefreshMarket입니다. 당신의 비밀번호는: " + userPasswd + "입니다.");
        mailSender.send(message);
    }
}
