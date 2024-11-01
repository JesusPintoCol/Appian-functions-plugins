package com.appian.encryption.utils;
import java.util.Base64;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class appian_generator_encrypt {
	private static final int GCM_TAG_LENGTH = 16; 
    private static final int GCM_IV_LENGTH = 12;
	public static SecretKeySpec getKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
	public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public static String encryptTextG(String plainText, SecretKeySpec key) throws Exception {
    	
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

       
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

       
        byte[] combined = new byte[GCM_IV_LENGTH + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedBytes, 0, combined, GCM_IV_LENGTH, encryptedBytes.length);

        
        return Base64.getEncoder().encodeToString(combined);
    }
    
    @Function
	public String getencrypttextwithkeyaes (@Parameter String keys, @Parameter String encrypt) {
    	String encryptedText=null;
		try {
           
           
            SecretKeySpec key = getKeyFromBase64(keys);
            
            
             encryptedText = encryptTextG(encrypt, key);

        } catch (Exception e) {
        	return "Error enconunter";
        }
		if(encryptedText==null)
			return "Error enconunter";
		else 
			return encryptedText;
	}
	
}
