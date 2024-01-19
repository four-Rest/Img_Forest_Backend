package com.ll.demo.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateRequestDto {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;


    // 기본 패스워드
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    // 확인 패스워드
    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    private String nickname;
}