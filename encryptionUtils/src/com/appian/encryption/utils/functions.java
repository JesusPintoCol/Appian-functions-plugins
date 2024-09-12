package com.appian.encryption.utils;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class functions {
	// key generator function
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
    // encryptor function
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
	
}
