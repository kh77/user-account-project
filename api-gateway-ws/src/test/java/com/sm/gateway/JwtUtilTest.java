package com.sm.gateway;

import com.sm.gateway.security.jwt.JwtProperty;
import com.sm.gateway.security.jwt.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
 class JwtUtilTest {
    private final JwtProperty jwtProperty = Mockito.mock(JwtProperty.class);

    private final JwtUtil jwtUtil = new JwtUtil(jwtProperty);


    @Test
    void testCreateToken() {
        String username = "testuser";
        List<String> roles = Arrays.asList("admin", "user");
        String secretKey = "testSecretKey";
        long expirationTime = 30000000;

        Mockito.when(jwtProperty.getExpireTime()).thenReturn(expirationTime);
        Mockito.when(jwtProperty.getSecretkey()).thenReturn(secretKey);

        String token = Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getExpireTime())) // 5 minute
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();


        assertTrue(jwtUtil.validateToken(token));


    }
}