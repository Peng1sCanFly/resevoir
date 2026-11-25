
package projectcsi.reservoir;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    // Generate a secure password hash.
    public static String hashPassword(String password) {

        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        byte[] hash = generateHash(password, salt);

        return Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    // Verify the password during login.
    public static boolean verifyPassword(
            String password, String storedPassword) {

        if (password == null || storedPassword == null) {
            return false;
        }

        try {
            String[] parts = storedPassword.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt =
                    Base64.getDecoder().decode(parts[0]);

            byte[] storedHash =
                    Base64.getDecoder().decode(parts[1]);

            byte[] enteredHash =
                    generateHash(password, salt);

            return MessageDigest.isEqual(
                    storedHash, enteredHash);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // PBKDF2 password hashing.
    private static byte[] generateHash(
            String password, byte[] salt) {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Password hashing failed.", e);

        } finally {
            spec.clearPassword();
        }
    }
}
