package com.java;

import java.util.Base64;

public class Base64Test {
    public static void main(String[] args) {
        String base64Message = "H4sIAAAAAAAA/8pIzknN0UjOzy/9Do1VQwPAAEAYclmJwAAAA="; // Replace with your Base64 string
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Message);
            System.out.println("Decoded successfully: " + decoded.length);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Base64 input: " + e.getMessage());
        }
    }
}
