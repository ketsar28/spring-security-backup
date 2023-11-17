//package com.express.security.util;
//
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.stream.Collectors;
//
//@Data
//@Service
//@RequiredArgsConstructor
//public class TokenService {
//    private final JwtEncoder jwtEncoder;
//    private final JwtDecoder jwtDecoder;
//
//    public String generateJwtToken(Authentication authentication) {
//        Instant now = Instant.now();
//
//        String scope = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(" "));
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .issuedAt(now)
//                .subject(authentication.getName())
//                .claim("roles", scope)
//                .build();
//
//        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//    }
//}
