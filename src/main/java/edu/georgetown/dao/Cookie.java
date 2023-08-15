package edu.georgetown.dao;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Cookie {

    private String cookieValue;

    public Cookie() {
        final String chrs = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();

            cookieValue = secureRandom
                    .ints(16, 0, chrs.length()) // 9 is the length of the string you want
                    .mapToObj(i -> chrs.charAt(i))
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public boolean checkCookie(String guess) {
        return cookieValue.equals(guess);
    }

    public static boolean checkCookie(String truth, String guess) {
        return truth.equals(guess);
    }


    public String toString() {
        return cookieValue;
    }

}
