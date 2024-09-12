package com.appian.data.encryption.generator;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class dataencryptiongenerator {

	
	// Convertir una clave en formato Base64 a SecretKeySpec
    public static SecretKeySpec getKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }

    // Método para cifrar texto usando AES
    public static String encrypt(String plainText, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    @Function
	public String dataencryptiongeneratorr (@Parameter String base64Key, @Parameter String plainText) {
    	String encryptedText=null;
		try {
           
            // Convertir la clave en un SecretKeySpec
            SecretKeySpec key = getKeyFromBase64(base64Key);
            
            // Cifrar el texto
             encryptedText = encrypt(plainText, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
		if(encryptedText==null)
			return "Fallo la encriptacion";
		else 
			return encryptedText;
	}
	
}
