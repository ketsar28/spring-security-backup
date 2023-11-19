package com.amigous.security2.service;

import com.amigous.security2.entity.Role;
import com.amigous.security2.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getAllUsers();
}
