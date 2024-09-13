package com.appian.encryption.utils;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.content.DocumentInputStream;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;


@Category("category.name.AppianScriptingFunctions")
public class functions {
	
	@Function
    public String getAesKey(){
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.init(256); 
        SecretKey key = keyGen.generateKey();

       
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        
        return base64Key;
    }
	
    public static SecretKeySpec getKeyFromBase(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }

    
    public static String encryptText(String plainText, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    @Function
	public String getEncryptedText (@Parameter String baseKey, @Parameter String Text) {
    	String encryptedText=null;
		try {
           
           
            SecretKeySpec key = getKeyFromBase(baseKey);
            
            
             encryptedText = encryptText(Text, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
		if(encryptedText==null)
			return "Error enconunter";
		else 
			return encryptedText;
	}

    
    public static String decryptText(String encryptedText, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
    @Function
    public String getDecryptedText (@Parameter String baseKey, @Parameter String Text ) {
    	String desencryptedText=null;
    	try {
            
            SecretKeySpec key = getKeyFromBase(baseKey);

            
             desencryptedText = decryptText(Text, key);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    	if(desencryptedText==null)
			return "Error enconunter";
		else 
			return desencryptedText;
    }
    
    
    @Function
	public String pemEncryption (ContentService cs,@Parameter String text, @DocumentDataType @Parameter Long document){
		String encryptedString=null;
		
		try {
			// Validate if document is valid before proceeding
			
			DocumentInputStream documentStream = cs.getDocumentInputStream(document);
			
			byte[] documentBytes = StreamUtil.inputStreamToBytes(documentStream);
					
			String result = bytesToString(documentBytes, StandardCharsets.UTF_8.name());

            // Load public key from PEM file
            PublicKey publicKey = loadPublicKey(result);

            // The string to encrypt
            String originalString = text;
            
            // Encrypt the string
            byte[] encryptedBytes = encrypt(originalString, publicKey);

            // Encode encrypted bytes to Base64 for readability
            encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
           
           
        } catch (Exception e) {
            e.printStackTrace();
        }
		
			return encryptedString;
		
		
	}
	
	public class StreamUtil {

	     
	    public static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
	        if (inputStream == null) {
	            throw new IllegalArgumentException("InputStream no puede ser nulo.");
	        }

	        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
	            byte[] buffer = new byte[1024];
	            int bytesRead;

	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                byteArrayOutputStream.write(buffer, 0, bytesRead);
	            }

	            return byteArrayOutputStream.toByteArray();
	        }
	    }
	}
	
	public static String bytesToString(byte[] bytes, String charset) {
        if (bytes == null) {
            throw new IllegalArgumentException("El array de bytes no puede ser nulo.");
        }
        if (charset == null || charset.isEmpty()) {
            throw new IllegalArgumentException("El charset no puede ser nulo o vacío.");
        }

        try {
            return new String(bytes, charset);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Charset no soportado: " + charset, e);
        }
    }
	
	public static PublicKey loadPublicKey(String pemData) throws Exception {
        
        String key = pemData.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                            .replaceAll("-----END PUBLIC KEY-----", "")
                            .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(key);
        
       
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePublic(spec);
    }

    public static byte[] encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
    
    
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_-+=<>?/[]{}|";
    @Function
    public String passwordGenerator(@Parameter String length ,@Parameter String type) {
        // Example usage
          // Desired password length
            // Type of characters (1: Numbers only, 2: Numbers and letters, 3: Numbers, letters, and special characters)

        String password = generatePassword(Integer.parseInt(length), Integer.parseInt(type));
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
    public static String generatePassword(int length, int type) {
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
                System.out.println("Invalid option. Defaulting to numbers only.");
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
