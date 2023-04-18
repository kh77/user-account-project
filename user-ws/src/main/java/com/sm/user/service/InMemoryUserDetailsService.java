package com.sm.user.service;

import com.sm.user.exception.ValidationException;
import com.sm.user.model.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    public static List<com.sm.user.model.AppUser> users = new ArrayList<>(Arrays.asList(new AppUser("admin", "{noop}admin", Arrays.asList(() -> "ROLE_ADMIN")), new AppUser("user1", "{noop}user", Arrays.asList(() -> "ROLE_USER")), new AppUser("user2", "{noop}user", Arrays.asList(() -> "ROLE_USER"))));

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream().filter(user -> user.getUsername().toLowerCase().equals(username)).findFirst().orElseThrow(() -> new ValidationException("Invalid username/password"));
    }
}
