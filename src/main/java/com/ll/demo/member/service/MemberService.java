package com.ll.demo.member.service;

import com.ll.demo.global.config.JwtProperties;
import com.ll.demo.global.config.JwtUtil;
import com.ll.demo.global.config.SecurityUser;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.dto.MyPageRequestDto;
import com.ll.demo.member.dto.MemberCreateRequestDto;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProperties jwtProperties;


    public GlobalResponse signup(MemberCreateRequestDto dto) {
        if (!validDuplicationUsername(dto.getUsername())) {
            return GlobalResponse.of("409", "중복된 이름입니다.");
        }
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword2()))
                .nickname(dto.getNickname())
                .email(dto.getEmail()).build();

        memberRepository.save(member);
        return GlobalResponse.of("200", "회원가입 완료");
    }

    public boolean validDuplicationUsername(String username) {
        return memberRepository.findByUsername(username).isPresent() ? false : true;
    }

    public GlobalResponse<Member> checkMembernameAndPassword(String username, String password) {
        Optional<Member> userOp = memberRepository.findByUsername(username);

        if (userOp.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        if (!encoder.matches(password, userOp.get().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return GlobalResponse.of("200", "로그인 성공", userOp.get());
    }

    public void setRefreshToken(Member member, String refreshToken) {
        memberRepository.save(member.toBuilder().refreshToken(refreshToken).build());
    }

    public Optional<Member> findUserByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public SecurityUser getMemberFromApiKey(String token) {
        Claims claims = JwtUtil.decode(token, jwtProperties.getSecretKey());

        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        long id = Long.parseLong((String) data.get("id"));
        String username = (String) data.get("username");
        List<? extends GrantedAuthority> authorities = ((List<String>) data.get("authorities"))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new SecurityUser(
                id,
                username,
                "",
                authorities
        );
    }

    public Member findByUsername(String username){
        Optional<Member> userOp = memberRepository.findByUsername(username);
        if(userOp.isPresent()){
            return userOp.get();
        }
        return null;
    }

    public GlobalResponse updateUserData(Member member, MyPageRequestDto dto) {
        String msg ="";
        if(dto.getNickname() != ""){
            member = member.toBuilder().nickname(dto.getNickname()).build();
            msg += "닉네임, ";
        }
        if(dto.getPassword1() != "" && dto.getPassword2() != ""){
            member = member.toBuilder().password(encoder.encode(dto.getPassword1())).build();
            msg += "비밀번호 ";
        }
        memberRepository.save(member);
        return GlobalResponse.of("200",msg + "변경완료");
    }
}
