package com.example.dynamodbtest.security.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {

    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        String[] parts = storedPassword.split("\\$");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String inputHash = hashPassword(inputPassword, salt); // 입력된 비밀번호 해싱
        return inputHash.equals(storedPassword); // 저장된 비밀번호와 비교
    }

    public static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt); // salt 추가
            byte[] hashedPassword = md.digest(password.getBytes());

            // 결과를 Base64로 인코딩하여 반환
            return Base64.getEncoder().encodeToString(salt) + "$" +
                    Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithm not found", e);
        }
    }

}
