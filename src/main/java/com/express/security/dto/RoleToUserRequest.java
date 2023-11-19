package com.express.security.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RoleToUserRequest {
    private String username;
    private String roleName;
}
