package com.ll.demo.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoUpdateRequestDto {

    private String username;
    private String password1;
    private String password2;
    private String email;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password1;
    }

    public void setPassword(String password) {
        this.password1 = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username  = username;
    }
}
