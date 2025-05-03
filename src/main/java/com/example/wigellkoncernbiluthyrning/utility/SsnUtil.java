package com.example.wigellkoncernbiluthyrning.utility;

public class SsnUtil {
    public static String SsnToPassword(String ssn) {
        String digitsOnly = ssn.replaceAll("\\D", "");

        if (digitsOnly.length() < 4) {
            throw new IllegalArgumentException("Ssn must be at least 4 characters");
        }
        return digitsOnly.substring(digitsOnly.length() - 4);
    }
}
