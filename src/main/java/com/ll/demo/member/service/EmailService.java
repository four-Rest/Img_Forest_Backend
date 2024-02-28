package com.ll.demo.member.service;

import com.ll.demo.member.entity.VerificationCode;
import com.ll.demo.member.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    // 인증 코드 생성 및 저장
    public VerificationCode createVerificationCode(String email) {
        VerificationCode code = VerificationCode.builder()
                .email(email)
                .code(UUID.randomUUID().toString()) // 랜덤 코드 생성
                .expiresTime(LocalDateTime.now().plusDays(1)) // 1일 후 만료
                .build();

        return emailRepository.save(code);
    }

    // 인증 코드 유효성 검사
    public boolean verifyCode(String email, String code) {
        return emailRepository.findByEmailAndCode(email, code)
                .map(vc -> vc.getExpiresTime().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    // 만료된 인증 코드 삭제 (스케줄링 작업 필요)
    public void deleteExpiredVerificationCodes() {
        emailRepository.deleteByExpiresTimeBefore(LocalDateTime.now());
    }
}
