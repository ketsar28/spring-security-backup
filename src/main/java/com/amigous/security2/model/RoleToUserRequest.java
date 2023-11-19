package com.amigous.security2.model;

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
