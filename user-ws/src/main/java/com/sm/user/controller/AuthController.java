package com.sm.user.controller;

import com.sm.user.controller.request.LoginRequest;
import com.sm.user.controller.response.TokenResponse;
import com.sm.user.model.AppUser;
import com.sm.user.security.jwt.JwtUtil;
import com.sm.user.service.TokenSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    private final TokenSessionService tokenSessionService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        try {
            String username = request.getUsername().toLowerCase();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            AppUser user = (AppUser) authentication.getPrincipal();
            if (tokenSessionService.isUserNameExist(username)) {
                throw new ValidationException("You are already login.");
            }
            List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return setTokenInCache(username, roles);
        } catch (BadCredentialsException ex) {
            throw new ValidationException("Invalid username/password");
        }
    }

    @GetMapping("/token")
    public TokenResponse getRefreshToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return setTokenInCache(username, roles);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String token = jwtUtil.getTokenReplaceBearer(request);
        String userName = jwtUtil.getUserNameByToken(token);
        tokenSessionService.deleteUserNameToken(userName);
        SecurityContextHolder.clearContext();
    }

    public TokenResponse setTokenInCache(String username, List<String> roles) {
        String token = jwtUtil.createToken(username, roles);
        tokenSessionService.add(username, token);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        tokenResponse.setUsername(username);
        tokenResponse.setRole(roles.get(0).toUpperCase());
        return tokenResponse;
    }

}