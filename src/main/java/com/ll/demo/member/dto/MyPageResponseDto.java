package com.ll.demo.member.dto;


import com.ll.demo.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageResponseDto {
    private String username;
    private String nickname;
    private String email;

    public MyPageResponseDto(Member member) {
        this.username  = member.getUsername();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
