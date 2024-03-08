package com.ll.demo.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEmailVerifyRequestDto {
    private String email;
    private String verificationCode;
}
