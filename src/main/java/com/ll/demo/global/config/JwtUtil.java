package com.ll.demo.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    // JWT 생성
    public static String encode(long expirationSeconds, Map<String, Object> data, String secretKey) {
        Claims claims= Jwts
                .claims()
                .setSubject("sb-23-11-30 jwt")
                .add("data", data)
                .build();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * expirationSeconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 검증
    public static Claims decode(String token, String secretKey) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
}