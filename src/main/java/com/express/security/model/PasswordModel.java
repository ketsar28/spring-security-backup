package com.express.security.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PasswordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
