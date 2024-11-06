package com.java;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomStringGenerator {

    // Define the characters to use in the string (A-Z, a-z, 0-9)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    // Method to generate a random alphanumeric string of a given length
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        
        return sb.toString();
    }

    public static void main(String[] args) {
        // 1MB = 1024 * 1024 bytes = 1048576 characters (since each character is 1 byte)
        int targetSizeInBytes = 1024 * 1024;  // 1MB
        
        // Generate the random string
        String randomString = generateRandomString(targetSizeInBytes);
        
        // Output the length of the string and the first 100 characters (for validation)
        System.out.println("Generated string length: " + randomString.length());
        System.out.println("First 100 characters of the string: " + randomString.substring(0, 100));
        
        // Uncomment this if you want to see the whole string (which will be 1MB)
        // System.out.println(randomString);
        
        try (FileWriter writer = new FileWriter("output.txt")) {
            writer.write(randomString);
            System.out.println("1MB file 'output.txt' generated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
