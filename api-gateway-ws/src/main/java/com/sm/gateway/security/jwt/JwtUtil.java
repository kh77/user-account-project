package com.sm.gateway.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperty jwtProperty;

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperty.getSecretkey()).parseClaimsJws(token);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public Claims getClaimsByToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperty.getSecretkey()).parseClaimsJws(token).getBody();
    }
}
