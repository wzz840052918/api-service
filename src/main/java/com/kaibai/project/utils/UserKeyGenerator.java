package com.kaibai.project.utils;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author 84005
 * @date 2023/11/24
 */
public class UserKeyGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ACCESS_KEY_LENGTH = 20;
    private static final int SECRET_KEY_LENGTH = 40;

    public static void main(String[] args) {
        String accessKey = generateAccessKey();
        String secretKey = generateSecretKey();

        System.out.println("Access Key: " + accessKey);
        System.out.println("Secret Key: " + secretKey);
    }

    public static String generateAccessKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder accessKey = new StringBuilder(ACCESS_KEY_LENGTH);

        for (int i = 0; i < ACCESS_KEY_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            accessKey.append(CHARACTERS.charAt(index));
        }

        return accessKey.toString();
    }

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder secretKey = new StringBuilder(SECRET_KEY_LENGTH);

        for (int i = 0; i < SECRET_KEY_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            secretKey.append(CHARACTERS.charAt(index));
        }

        return Base64.getEncoder().encodeToString(secretKey.toString().getBytes());
    }
}
