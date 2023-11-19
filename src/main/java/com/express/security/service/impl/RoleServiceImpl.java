package com.express.security.service.impl;

import com.express.security.dto.RoleToUserRequest;
import com.express.security.entity.Role;
import com.express.security.entity.User;
import com.express.security.repository.RoleRepository;
import com.express.security.repository.UserRepository;
import com.express.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Role saveRole(Role request) {
        return roleRepository.save(request);
    }

    @Override
    public void addRoleToUser(RoleToUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        Role role = roleRepository.findByName(request.getRoleName());
        if(Objects.isNull(user)) {
            throw new ResponseStatusException(NOT_FOUND, "User not found");
        }
        if(Objects.isNull(role)) {
            throw new ResponseStatusException(NOT_FOUND, "Role not found");
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
