package com.ll.demo.member.dto;

import com.ll.demo.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String username;
    private String nickname;



    public LoginResponseDto(Member member) {
        this.username  = member.getUsername();
        this.nickname = member.getNickname();
    }
}
