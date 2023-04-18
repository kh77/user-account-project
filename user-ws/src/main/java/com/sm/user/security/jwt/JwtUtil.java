package com.sm.user.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;

    public String createToken(String username, List<String> roles) {
        return Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getExpireTime())) // 5 minute
                .signWith(SignatureAlgorithm.HS512, jwtProperty.getSecretkey()).compact();
    }

    public String getUserNameByToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperty.getSecretkey()).parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenReplaceBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.replace("Bearer ", "");
    }

    public Claims getClaimsByToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperty.getSecretkey()).parseClaimsJws(token).getBody();
    }


}
