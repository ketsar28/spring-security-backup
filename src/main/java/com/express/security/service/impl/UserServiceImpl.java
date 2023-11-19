package com.express.security.service.impl;

import com.express.security.config.BCryptUtil;
import com.express.security.entity.User;
import com.express.security.entity.UserDetailsApp;
import com.express.security.repository.RoleRepository;
import com.express.security.repository.UserRepository;
import com.express.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private  final BCryptUtil bCryptUtil;
    @Override
    public User saveUser(User request) {
        request.setPassword(bCryptUtil.hashPassword(request.getPassword()));
        return userRepository.save(request);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public UserDetails findUserBaseOnUsername(String username) {
//        return userDetails;
//    }
}
