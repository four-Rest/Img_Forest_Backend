package com.ll.demo.user.dto;


import com.ll.demo.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageResponseDto {
    private String username;
    private String nickname;
    private String email;

    public MyPageResponseDto(User user) {
        this.username  = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
