package com.sm.account.service;

import com.sm.account.exception.ValidationException;
import com.sm.account.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    public static List<User> users = new ArrayList<>(Arrays.asList(
            new User("admin", "{noop}admin", Arrays.asList(() -> "ROLE_ADMIN")),
            new User("user1", "{noop}user", Arrays.asList(() -> "ROLE_USER")),
            new User("user2", "{noop}user", Arrays.asList(() -> "ROLE_USER"))
    ));

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().toLowerCase().equals(username))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Invalid username/password"));
    }
}
