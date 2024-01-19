package com.ll.demo.member.controller;

import com.ll.demo.global.config.JwtProperties;
import com.ll.demo.global.config.JwtUtil;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.member.dto.*;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final MemberService memberService;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final JwtProperties jwtProperties;

    @PostMapping("/signup")
    public GlobalResponse signup(@RequestBody MemberCreateRequestDto userCreateRequestDto) {
        if (!userCreateRequestDto.getPassword1().equals(userCreateRequestDto.getPassword2())) {
            return GlobalResponse.of("409", "비밀번호가 일치하지 않습니다");
        }
        return memberService.signup(userCreateRequestDto);
    }

    @PostMapping("/login")
    public GlobalResponse<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        GlobalResponse<Member> checkedResp = memberService.checkUsernameAndPassword(dto.getUsername(), dto.getPassword());
        Member member = checkedResp.getData();
        String accessToken = JwtUtil.encode(
                60 * 10, // 1 minute
                Map.of(
                        "id", member.getId().toString(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthoritiesAsStrList()
                )
                , jwtProperties.getSECRET_KEY()
        );
        String refreshToken = JwtUtil.encode(
                60 * 60 * 24, //1 day
                Map.of(
                        "id", member.getId().toString(),
                        "username", member.getUsername()
                )
                , jwtProperties.getSECRET_KEY()
        );
        memberService.setRefreshToken(member, refreshToken);

        addCrossDomainCookie(accessToken, refreshToken);

        return GlobalResponse.of("200", "로그인 성공.", new LoginResponseDto(member));
    }

    @PostMapping("/login/refresh")
    public GlobalResponse refreshAccessToken() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return GlobalResponse.of("401", "cookies not exist.");
        }
        Optional<Cookie> refreshTokenCookieOp = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst();
        if (refreshTokenCookieOp.isEmpty()) {
            return GlobalResponse.of("401", "refreshToken not exist.");
        }

        String refreshToken = refreshTokenCookieOp.get().getValue();
        Member member = memberService.findUserByRefreshToken(refreshToken).get();
        String accessToken = JwtUtil.encode(
                60 * 10,
                Map.of(
                        "id", member.getId().toString(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthoritiesAsStrList()
                ),
                jwtProperties.getSECRET_KEY()
        );
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .maxAge(60 * 10)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());
        return GlobalResponse.of("200", "refresh accessToken complete");
    }

    @PostMapping("/logout")
    public GlobalResponse logout() {

        removeCrossDomainCookie();
        return GlobalResponse.of("200", "로그아웃 성공");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public GlobalResponse<MyPageResponseDto> mypage(Principal principal) {

        String username = principal.getName();
        MyPageResponseDto responseDto = new MyPageResponseDto(memberService.findByUsername(username));
        return GlobalResponse.of("200", "유저 정보 반환", responseDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public GlobalResponse mypage(Principal principal, @RequestBody MyPageRequestDto dto){


        Member member = memberService.findByUsername(principal.getName());
        if(member != null){
            return memberService.updateUserData(member, dto);
        } else{
            return GlobalResponse.of("403", "확인되지 않은 유저입니다.");
        }
    }

    private void removeCrossDomainCookie() {
        ResponseCookie cookie1 = ResponseCookie.from("accessToken", null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        ResponseCookie cookie2 = ResponseCookie.from("refreshToken", null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", cookie1.toString());
        response.addHeader("Set-Cookie", cookie2.toString());
    }

    private void addCrossDomainCookie(String accessToken, String refreshToken) {
        ResponseCookie cookie1 = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .maxAge(60 * 10)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        ResponseCookie cookie2 = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(60 * 60 * 24)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", cookie1.toString());
        response.addHeader("Set-Cookie", cookie2.toString());
    }


}