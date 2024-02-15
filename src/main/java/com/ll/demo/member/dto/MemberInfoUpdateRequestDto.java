package com.ll.demo.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoUpdateRequestDto {

    private String loginId;
    private String password;
    private String email;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId  = loginId;
    }
}
