package com.ll.demo.user.controller;

import com.ll.demo.global.config.JwtProperties;
import com.ll.demo.global.config.JwtUtil;
import com.ll.demo.global.response.GlobalResponse;
import com.ll.demo.user.dto.*;
import com.ll.demo.user.entity.User;
import com.ll.demo.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final JwtProperties jwtProperties;

    @PostMapping("/signup")
    public GlobalResponse signup(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        if (!userCreateRequestDto.getPassword1().equals(userCreateRequestDto.getPassword2())) {
            return GlobalResponse.of("409", "비밀번호가 일치하지 않습니다");
        }
        return userService.signup(userCreateRequestDto);
    }

    @PostMapping("/login")
    public GlobalResponse<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        GlobalResponse<User> checkedResp = userService.checkUsernameAndPassword(dto.getUsername(), dto.getPassword());
        User user = checkedResp.getData();
        String accessToken = JwtUtil.encode(
                60 * 10, // 1 minute
                Map.of(
                        "id", user.getId().toString(),
                        "username", user.getUsername(),
                        "authorities", user.getAuthoritiesAsStrList()
                )
                , jwtProperties.getSECRET_KEY()
        );
        String refreshToken = JwtUtil.encode(
                60 * 60 * 24, //1 day
                Map.of(
                        "id", user.getId().toString(),
                        "username", user.getUsername()
                )
                , jwtProperties.getSECRET_KEY()
        );
        userService.setRefreshToken(user, refreshToken);

        addCrossDomainCookie(accessToken, refreshToken);

        return GlobalResponse.of("200", "로그인 성공.", new LoginResponseDto(user));
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
        User user = userService.findUserByRefreshToken(refreshToken).get();
        String accessToken = JwtUtil.encode(
                60 * 10,
                Map.of(
                        "id", user.getId().toString(),
                        "username", user.getUsername(),
                        "authorities", user.getAuthoritiesAsStrList()
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
        MyPageResponseDto responseDto = new MyPageResponseDto(userService.findByUsername(username));
        return GlobalResponse.of("200", "유저 정보 반환", responseDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public GlobalResponse mypage(Principal principal, @RequestBody MyPageRequestDto dto){


        User user = userService.findByUsername(principal.getName());
        if(user != null){
            return userService.updateUserData(user, dto);
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