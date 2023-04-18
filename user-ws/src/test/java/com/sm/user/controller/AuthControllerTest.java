package com.sm.user.controller;


import com.sm.user.controller.request.LoginRequest;
import com.sm.user.controller.response.TokenResponse;
import com.sm.user.model.AppUser;
import com.sm.user.security.jwt.JwtUtil;
import com.sm.user.service.TokenSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TokenSessionService tokenSessionService;
    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    void loginTest() {
        String username = "testuser";
        String password = "testpassword";
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        TokenSessionService tokenSessionService = mock(TokenSessionService.class);
        AuthController authController = new AuthController(authenticationManager, jwtUtil, tokenSessionService);

        Authentication authentication = mock(Authentication.class);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");
        AppUser appUser = new AppUser(username, password, authorities);
        String token = "testtoken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(appUser);

        when(tokenSessionService.isUserNameExist(username)).thenReturn(false);
        when(jwtUtil.createToken(username, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))).thenReturn(token);

        TokenResponse response = authController.login(request);

        assertNotNull(response);
        assertEquals(username, response.getUsername());
        assertEquals(token, response.getToken());
        assertEquals(authorities.get(0).getAuthority().toUpperCase(), response.getRole());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        //   verify(appUser).getAuthorities();
        verify(tokenSessionService).isUserNameExist(username);
        verify(jwtUtil).createToken(username, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        verify(tokenSessionService).add(username, token);
    }


    @Test
    void loginWithExistingUserNameTest() {
        String username = "testuser";
        String password = "testpassword";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        Authentication authentication = mock(Authentication.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");
        AppUser appUser = new AppUser(username, password, authorities);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(tokenSessionService.isUserNameExist(username)).thenReturn(true);

        assertThrows(ValidationException.class, () -> {
            authController.login(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenSessionService).isUserNameExist(username);
        verify(jwtUtil, never()).createToken(anyString(), anyList());
        verify(tokenSessionService, never()).add(anyString(), anyString());
    }

    @Test
    void testLogout() {
        String token = "testToken";
        String userName = "testuser";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtUtil.getTokenReplaceBearer(request)).thenReturn(token);
        when(jwtUtil.getUserNameByToken(token)).thenReturn(userName);

        // Save the original SecurityContextHolderStrategy
        SecurityContextHolderStrategy originalStrategy = SecurityContextHolder.getContextHolderStrategy();

        try {
            // Set a custom SecurityContextHolderStrategy for testing
            SecurityContextHolder.setContextHolderStrategy(new SecurityContextHolderStrategy() {
                @Override
                public void clearContext() {
                    // Do nothing
                }

                @Override
                public SecurityContext getContext() {
                    return new SecurityContextImpl();
                }

                @Override
                public void setContext(SecurityContext context) {
                    // Do nothing
                }

                @Override
                public SecurityContext createEmptyContext() {
                    return null;
                }
            });

            // Act
            authController.logout(request);

            // Verify
            verify(tokenSessionService).deleteUserNameToken(userName);
        } finally {
            // Restore the original SecurityContextHolderStrategy
            SecurityContextHolder.setContextHolderStrategy(originalStrategy);
        }
    }

}

