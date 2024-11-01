package com.appian.encryption.utils;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class appian_generator_decrypt {
	private static final int GCM_TAG_LENGTH = 16; 
    private static final int GCM_IV_LENGTH = 12; 
	public static SecretKeySpec getKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
	 public static byte[] hexToBytes(String hex) {
	        int length = hex.length();
	        byte[] data = new byte[length / 2];
	        for (int i = 0; i < length; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                                 + Character.digit(hex.charAt(i + 1), 16));
	        }
	        return data;
	    }
	 
	 public static String decryptTextG(String encryptedText, SecretKeySpec key) throws Exception {
		    byte[] combined = Base64.getDecoder().decode(encryptedText);
	        
	       
	        byte[] iv = new byte[GCM_IV_LENGTH];
	        System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);

	       
	        byte[] cipherText = new byte[combined.length - GCM_IV_LENGTH];
	        System.arraycopy(combined, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

	        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
	        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
	        cipher.init(Cipher.DECRYPT_MODE, key, spec);

	        byte[] decryptedBytes = cipher.doFinal(cipherText);
	        return new String(decryptedBytes, "UTF-8");
	    }
	    @Function
	    public String getdecrypttextwithkeyaes (@Parameter String keys, @Parameter String decrypt ) {
	    	String desencryptedText=null;
	    	try {
	            
	            SecretKeySpec key = getKeyFromBase64(keys);

	            
	             desencryptedText = decryptTextG(decrypt, key);
	            

	        } catch (Exception e) {
	        	return "Error enconunter";
	        }
	    	if(desencryptedText==null)
				return "Error enconunter";
			else 
				return desencryptedText;
	    }
}
