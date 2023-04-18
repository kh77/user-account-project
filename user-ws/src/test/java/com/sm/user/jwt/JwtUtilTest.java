package com.sm.user.jwt;

import com.sm.user.security.jwt.JwtProperty;
import com.sm.user.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        String token = jwtUtil.createToken(username, roles);
        Claims claims = jwtUtil.getClaimsByToken(token);

        assertEquals(username, claims.getSubject());
        assertEquals(roles, claims.get("roles"));
        assertEquals(expirationTime, claims.getExpiration().getTime() - claims.getIssuedAt().getTime());
    }

    @Test
    void testGetUserNameByToken() {
        String username = "testuser";
        String secretKey = "testSecretKey";
        long expirationTime = new Date(System.currentTimeMillis() + 30000000).getTime();
        Mockito.when(jwtProperty.getExpireTime()).thenReturn(expirationTime);
        Mockito.when(jwtProperty.getSecretkey()).thenReturn(secretKey);
        String token = jwtUtil.createToken(username, Arrays.asList("ADMIN", "USER"));
        assertEquals(username, jwtUtil.getUserNameByToken(token));
    }

    @Test
    void testGetTokenReplaceBearer() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        String token = "test_token";

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        assertEquals(token, jwtUtil.getTokenReplaceBearer(request));
    }
}
