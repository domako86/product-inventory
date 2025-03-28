package com.example.productinventory.security.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] keyBytes = new byte[32]; // 256 bits
        new java.security.SecureRandom().nextBytes(keyBytes);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(base64Key);
    }
}
