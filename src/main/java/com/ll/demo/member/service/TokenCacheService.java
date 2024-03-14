package com.ll.demo.member.service;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenCacheService {

    // 토큰 캐시를 저장할 Map 선언
    private final Map<String, Boolean> tokenCache = new ConcurrentHashMap<>();

    // 토큰이 캐시에 있는지 확인하는 메서드
    public boolean isTokenCached(String token) {
        return tokenCache.containsKey(token);
    }

    // 토큰을 캐시에 추가하는 메서드
    public void cacheToken(String token) {
        tokenCache.put(token, true);
    }

    // 토큰을 캐시에서 제거하는 메서드 (만료된 경우)
    public void removeToken(String token) {
        tokenCache.remove(token);
    }
}
