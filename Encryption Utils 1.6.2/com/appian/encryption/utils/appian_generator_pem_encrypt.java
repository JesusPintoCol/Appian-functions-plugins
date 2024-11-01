package com.appian.encryption.utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.content.DocumentInputStream;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;

@Category("category.name.AppianScriptingFunctions")
public class appian_generator_pem_encrypt {
	@Function
	public String getpemtextencryptwithpemfile(ContentService cs,@Parameter String text, @DocumentDataType @Parameter Long document){
		String encryptedString=null;
		
		try {
			// Validate if document is valid before proceeding
			
			DocumentInputStream documentStream = cs.getDocumentInputStream(document);
			
			byte[] documentBytes = StreamUtils.inputStreamToBytes(documentStream);
					
			String result = bytesToStrings(documentBytes, StandardCharsets.UTF_8.name());

            // Load public key from PEM file
            PublicKey publicKey = loadPublicKeys(result);

            // The string to encrypt
            String originalString = text;
            
            // Encrypt the string
            byte[] encryptedBytes = encryptG(originalString, publicKey);

            // Encode encrypted bytes to Base64 for readability
            encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
           
           
        } catch (Exception e) {
        	return "Error enconunter";
        }
		
			return encryptedString;
		
		
	}
	
	public class StreamUtils {

	     
	    public static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
	        if (inputStream == null) {
	            throw new IllegalArgumentException("null");
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
	  public static SecretKeySpec getKeyFromBase64(String base64Key) {
	        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
	        return new SecretKeySpec(decodedKey, "AES");
	    }

	    
	    public static String encryptTextG(String plainText, SecretKeySpec key) throws Exception {
	        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
	        return Base64.getEncoder().encodeToString(encryptedBytes);
	    }
	public static String bytesToStrings(byte[] bytes, String charset) {
        if (bytes == null) {
            throw new IllegalArgumentException("null");
        }
        if (charset == null || charset.isEmpty()) {
            throw new IllegalArgumentException("null");
        }

        try {
            return new String(bytes, charset);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Error " + charset, e);
        }
    }
	
	public static PublicKey loadPublicKeys(String pemData) throws Exception {
        
        String key = pemData.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                            .replaceAll("-----END PUBLIC KEY-----", "")
                            .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(key);
        
       
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePublic(spec);
    }

    public static byte[] encryptG(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
