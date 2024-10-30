package com.poly.greeen.Utils;

import java.util.UUID;

public class AuthorizationCodeGenerator {
    public static String generateAuthorizationCode() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        String authCode = generateAuthorizationCode();
        System.out.println("Generated Authorization Code: " + authCode);
    }
}