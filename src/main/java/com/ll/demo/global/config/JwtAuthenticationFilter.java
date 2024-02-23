package com.ll.demo.global.config;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProperties jwtProperties;


    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String apiKey = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> rqCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("accessToken"))
                    .findFirst();
            if (rqCookie.isPresent()) {
                apiKey = rqCookie.get().getValue();
            }
        }

        if (apiKey != null) {
            SecurityUser user = getMemberFromApiKey(apiKey);
            setAuthentication(user);
        }

        filterChain.doFilter(request, response);
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

    public void setAuthentication(SecurityUser member) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                member,
                member.getPassword(),
                member.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}