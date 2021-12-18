package kh.com.sathapana.merchant.common;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    private PasswordUtils() {

    }
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String hashedPassword, String rawPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
