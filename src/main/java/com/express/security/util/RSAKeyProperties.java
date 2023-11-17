//package com.express.security.util;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.stereotype.Component;
//
//import java.security.KeyPair;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//
//@Component
//@Setter
//@Getter
//public class RSAKeyProperties {
//    private RSAPublicKey publicKey;
//    private RSAPrivateKey privateKey;
//
//    public RSAKeyProperties() {
//        KeyPair keyPair = KeyGenerateUtility.generateResaKey();
//        this.publicKey = (RSAPublicKey) keyPair.getPublic();
//        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
//    }
//}
