package com.sm.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class InMemoryUserDetailsServiceTest {
    @InjectMocks
    private InMemoryUserDetailsService inMemoryUserDetailsService;

    @Test
    public void testLoadUserByUsername_InvalidUsernamePassword() {

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> inMemoryUserDetailsService.loadUserByUsername("invaliduser"),
                "Invalid username/password"
        );

        assertTrue(thrown.getMessage().contentEquals("Invalid username/password"));
    }

    @Test
    public void testLoadUserByUsername_ValidUsernamePassword() {
        UserDetails userDetails = inMemoryUserDetailsService.loadUserByUsername("admin");
        assertNotNull(userDetails);
        assertEquals("admin",userDetails.getUsername());
    }
}
