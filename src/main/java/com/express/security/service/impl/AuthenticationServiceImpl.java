//package com.express.security.service.impl;
//
//import com.express.security.model.LoginResponse;
//import com.express.security.entity.Role;
//import com.express.security.entity.User;
//import com.express.security.repository.RoleRepository;
//import com.express.security.repository.UserRepository;
//import com.express.security.util.TokenService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationServiceImpl {
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    private final TokenService tokenService;
//
//    private final AuthenticationManager authenticationManager;
//
//    @Transactional(rollbackOn = Exception.class)
//    public User registerUser(String username, String password) {
//        String encode = passwordEncoder.encode(password);
//        Role userRole = roleRepository.findByAuthority("USER").get();
//        Set<Role> authorities = new HashSet<>();
//        authorities.add(userRole);
//
//        return userRepository.save(new User(UUID.randomUUID().toString(), username, encode, authorities));
//    }
//
//    public List<User> getAllUser() {
//        return userRepository.findAll();
//    }
//
//    public LoginResponse loginUser(String username, String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//              new UsernamePasswordAuthenticationToken(username, password)
//            );
//            String token = tokenService.generateJwtToken(authentication);
//            return new LoginResponse(userRepository.findByUsername(username).get(), token);
//        } catch (AuthenticationException exception) {
//            return new LoginResponse(null, "");
//        }
//    }
//}
