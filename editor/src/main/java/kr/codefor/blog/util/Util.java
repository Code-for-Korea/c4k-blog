package kr.codefor.blog.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Util {

    public String sha256Encrypt(String plainText) {
        // SHA256 암호화
        String encryptResult = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes(StandardCharsets.UTF_8));
            encryptResult = String.format("%064X", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return encryptResult;
    }

    private String generateRandomStringKey(int targetStringLength) {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }
}
