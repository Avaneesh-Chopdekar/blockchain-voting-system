package utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;

    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash the password with salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);

            byte[] hashedPassword = digest.digest(password.getBytes("UTF-8"));

            // Perform multiple iterations
            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hashedPassword = digest.digest(hashedPassword);
            }

            // Combine salt and hashed password
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            // Convert to Base64
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        try {
            // Decode the stored hash
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extract salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, salt.length);

            // Extract hashed password
            byte[] storedPasswordHash = new byte[combined.length - salt.length];
            System.arraycopy(combined, salt.length, storedPasswordHash, 0, storedPasswordHash.length);

            // Hash input password with extracted salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);

            byte[] hashedInputPassword = digest.digest(inputPassword.getBytes("UTF-8"));

            // Perform multiple iterations
            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hashedInputPassword = digest.digest(hashedInputPassword);
            }

            // Compare hashes
            return MessageDigest.isEqual(hashedInputPassword, storedPasswordHash);
        } catch (Exception e) {
            throw new RuntimeException("Password verification failed", e);
        }
    }
}