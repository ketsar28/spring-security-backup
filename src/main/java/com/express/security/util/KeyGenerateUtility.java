//package com.express.security.util;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//
//public class KeyGenerateUtility {
//    public static KeyPair generateResaKey() {
//        KeyPair keyPair;
//        try {
//            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//            generator.initialize(2048);
//            keyPair = generator.generateKeyPair();
//        } catch (Exception e) {
//            throw new IllegalStateException();
//        }
//        return keyPair;
//    }
//}
