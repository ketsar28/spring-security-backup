package com.express.security.service;

import com.express.security.dto.RoleToUserRequest;
import com.express.security.entity.Role;

public interface RoleService {
    Role saveRole(Role request);
    void addRoleToUser(RoleToUserRequest request);
}
