package com.express.security.service;

import com.express.security.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public interface UserService {
    User saveUser(User request);
    User findUserByUsername(String username);
    List<User> findAllUsers();
//    UserDetails findUserBaseOnUsername(String username);
}
