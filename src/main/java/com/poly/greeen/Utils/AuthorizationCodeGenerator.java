package com.poly.greeen.Utils;

import java.util.Random;
import java.util.UUID;

public class AuthorizationCodeGenerator {
    public static String generateAuthorizationCode() {
        return UUID.randomUUID().toString();
    }

    public static String generateRandomOTP() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // Generates a random number between 100000 and 999999
        return String.valueOf(number);
    }

    public static void main(String[] args) {
        String authCode = generateAuthorizationCode();
        System.out.println("Generated Authorization Code: " + authCode);
    }
}