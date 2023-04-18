package com.sm.user.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


import com.sm.user.security.jwt.JwtProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenSessionService {
    private Cache<String, String> cache;
    private final JwtProperty jwtProperty;

    @PostConstruct
    public void init() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Long.valueOf(jwtProperty.getExpiryInMinute()), TimeUnit.MINUTES)
                .build();
    }

    public void add(String username, String token) {
        cache.put(username, token);
    }

    public boolean isTokenAndUserNameExist(String username, String token) {
        String cachedToken = cache.getIfPresent(username);
        return cachedToken != null && cachedToken.equals(token);
    }

    public boolean isUserNameExist(String username) {
        String cachedToken = cache.getIfPresent(username);
        return cachedToken != null;
    }

    public void deleteUserNameToken(String username) {
        cache.invalidate(username);
    }
}
