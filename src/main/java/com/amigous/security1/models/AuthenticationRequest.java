package com.amigous.security1.models;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthenticationRequest {
    private String email;
    private String password;
}
