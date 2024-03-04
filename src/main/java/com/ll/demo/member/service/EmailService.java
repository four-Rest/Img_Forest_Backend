package com.ll.demo.member.service;

import com.ll.demo.member.entity.VerificationCode;
import com.ll.demo.member.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail,String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        System.out.println(emailForm);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            e.printStackTrace(); // 또는 로거를 사용하여 상세한 예외 정보 로깅
            throw new RuntimeException("Unable to send email in sendEmail", e); // 원인 예외를 포함시키기
        }
    }

    public SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }


}
