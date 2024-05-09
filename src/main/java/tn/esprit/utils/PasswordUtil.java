package tn.esprit.utils;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hash a password using BCrypt.
     * @param plainTextPassword the password to hash
     * @return a hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(13));
    }

    /**
     * Check if an unencrypted password matches one that has previously been hashed.
     * @param plainTextPassword the unencrypted password
     * @param hashedPassword the previously-hashed password
     * @return true if the passwords match, false otherwise
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}
