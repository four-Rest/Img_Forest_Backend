package com.ll.demo.member.entity;

import com.ll.demo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class VerificationCode extends BaseEntity {

    @Column(nullable = false)
    private Long memberId; // 회원 번호

    @Column(nullable = false, length = 64)
    private String code; // 인증 코드

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 만료 시간

}
