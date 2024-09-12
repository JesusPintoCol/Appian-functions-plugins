package com.appian.data.desencryption.generator;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class datadesencryptiongenerator {
	// Convertir una clave en formato Base64 a SecretKeySpec
    public static SecretKeySpec getKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }

    // Método para descifrar texto cifrado usando AES
    public static String decrypt(String encryptedText, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
    @Function
    public String datadesencryptiongeneratorr (@Parameter String base64Key, @Parameter String encryptedText ) {
    	String desencryptedText=null;
    	try {
            // Convertir la clave en un SecretKeySpec
            SecretKeySpec key = getKeyFromBase64(base64Key);

            // Descifrar el texto
             desencryptedText = decrypt(encryptedText, key);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    	if(desencryptedText==null)
			return "Fallo la encriptacion";
		else 
			return desencryptedText;
    }
    
}
