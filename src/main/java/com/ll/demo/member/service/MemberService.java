package com.ll.demo.member.service;

import com.ll.demo.global.config.JwtProperties;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.dto.MemberCreateRequestDto;
import com.ll.demo.member.dto.MemberInfoUpdateRequestDto;
import com.ll.demo.member.dto.MyPageRequestDto;
import com.ll.demo.member.dto.kakkoMemberCreateRequestDto;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProperties jwtProperties;

    @Transactional
    public GlobalResponse<Member> signup(MemberCreateRequestDto dto) {
        if (validDuplicationUsername(dto.getUsername()).isPresent()) {
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

    @Transactional
    public GlobalResponse<Member> socialSignup(kakkoMemberCreateRequestDto dto) {
        if (validDuplicationUsername(dto.getUsername()).isPresent()) {
            return GlobalResponse.of("409", "중복된 이름입니다.");
        }
        Member member = Member.builder()
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .build();

        memberRepository.saveAndFlush(member);
        return GlobalResponse.of("200", "회원가입 완료", member);
    }

    @Transactional
    public GlobalResponse<Member> updateMemberInfo(Member member, MemberInfoUpdateRequestDto requestDto) {
        String msg = "";
        if (requestDto.getNickname() != null && !requestDto.getNickname().isEmpty()) {
            member.setNickname(requestDto.getNickname());
            msg += "닉네임, ";
        }
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            member.setPassword(encoder.encode(requestDto.getPassword()));
            msg += "비밀번호, ";
        }
        if (requestDto.getEmail() != null && !requestDto.getEmail().isEmpty()) {
            member.setEmail(requestDto.getEmail());
            msg += "이메일 ";
        }
        memberRepository.save(member);
        return GlobalResponse.of("200", msg + "변경 완료", member);
    }

    public Optional<Member> validDuplicationUsername(String username) {
        return memberRepository.findByUsername(username);
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

    @Transactional
    public GlobalResponse<Member> whenSocialLogin(String providerTypeCode, String username, String nickname, String profileImgUrl) {
        Optional<Member> opMember = validDuplicationUsername(username);

        if (opMember.isPresent()) return GlobalResponse.of("200", "이미 존재합니다.", opMember.get());

        kakkoMemberCreateRequestDto dto = new kakkoMemberCreateRequestDto();
        dto.setUsername(username);
        dto.setNickname(nickname);
        return socialSignup(dto);
    }
}
