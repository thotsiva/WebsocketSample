package com.java;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomStringGeneratorWithLines {

    public static void main(String[] args) {
        // Target size in bytes (500 KB = 500 * 1024 bytes)
        final int targetSizeBytes = 1024 * 1024;
        final int lineLength = 100; // Length of each line of random string
        int totalWritten = 0;

        // Random object for generating random characters
        Random random = new Random();

        // Try to write to a file
        try (FileWriter writer = new FileWriter("random_strings.txt")) {
            while (totalWritten < targetSizeBytes) {
                // Generate random string of length 'lineLength'
                String randomString = generateRandomString(lineLength, random);

                // Write the string followed by a newline character
                writer.write(randomString + "\n");

                // Update the total number of bytes written
                totalWritten += randomString.length() + 1;  // +1 for the newline character
            }
            System.out.println("Random string file of approximately 500 KB generated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a random string of specified length
    private static String generateRandomString(int length, Random random) {
        StringBuilder sb = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}
