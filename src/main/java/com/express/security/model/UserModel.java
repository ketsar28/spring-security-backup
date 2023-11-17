package com.express.security.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}
