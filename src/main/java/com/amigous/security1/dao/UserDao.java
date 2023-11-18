package com.amigous.security1.dao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDao {
    private static final List<UserDetails> APPLICATION_USERS = Arrays.asList(
            new User(
                    "ketsar@gmail.com",
                    "ketsar",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ),
            new User(
                    "nabil@co.id",
                    "nabil",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            )
    );

    public UserDetails findUserByEmail(String email) {
        return APPLICATION_USERS
                .stream()
                .filter(user -> user.getUsername().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(email + " is not found"));
    }
}
