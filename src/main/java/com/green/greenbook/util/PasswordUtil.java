package com.green.greenbook.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCrypt;

@UtilityClass
public class PasswordUtil {

    public static boolean checkPassword(String input, String encrypedPassword) {
        return BCrypt.checkpw(input, encrypedPassword);
    }

}