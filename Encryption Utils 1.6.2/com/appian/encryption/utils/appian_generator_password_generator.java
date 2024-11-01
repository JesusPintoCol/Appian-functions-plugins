package com.appian.encryption.utils;
import java.security.SecureRandom;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class appian_generator_password_generator {
	private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_-+=<>?/[]{}|";
    @Function
    public String getrandompasswordgeneratorstring(@Parameter String length ,@Parameter String type) {
  

        String password = generatePasswordG(Integer.parseInt(length), Integer.parseInt(type));
        return password;
        /**
         * Generates a password based on the length and type of characters specified.
         *
         * @param length The length of the password.
         * @param type The type of characters to include in the password:
         *             1 - Numbers only
         *             2 - Numbers and letters
         *             3 - Numbers, letters, and special characters
         * @return The generated password.
         */
        
    }
    public static String generatePasswordG(int length, int type) {
        String characters;
        switch (type) {
            case 1:
                characters = DIGITS;
                break;
            case 2:
                characters = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS;
                break;
            case 3:
                characters = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS + SPECIAL_CHARACTERS;
                break;
            default:
            	
                characters = DIGITS;
                
                break;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
}
