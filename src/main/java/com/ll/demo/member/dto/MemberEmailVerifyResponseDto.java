package com.ll.demo.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEmailVerifyResponseDto {
    private boolean verified;
    private String message;
}
