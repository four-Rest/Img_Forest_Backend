package com.ll.demo.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoUpdateResponseDto {

    private String loginId;
    private String password;
    private String email;
    private String nickname;
}
