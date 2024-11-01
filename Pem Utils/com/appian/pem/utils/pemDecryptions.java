package com.appian.pem.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.content.DocumentInputStream;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;
@Category("category.name.AppianScriptingFunctions")
public class pemDecryptions {
	@Function
	public String pemDecryption(ContentService cs,@Parameter String text, @DocumentDataType @Parameter Long document){
		String decryptedString=null;
		try {
		DocumentInputStream documentStream = cs.getDocumentInputStream(document);
		
		byte[] documentBytes = StreamUtils.inputStreamToBytes(documentStream);
		documentStream.close();		
		String result = bytesToStrings(documentBytes, StandardCharsets.UTF_8.name());
		PrivateKey privateKey = loadPrivateKey(result);
        byte[] combined = Base64.getDecoder().decode(text);
		decryptedString = decrypt(combined, privateKey);
         
		}
		catch (Exception e) {
        	return "Error enconunter";
        }
		return decryptedString;
	}
	public static String decrypt(byte[] encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedText);
        return new String(decryptedBytes);
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
	
	public static PrivateKey loadPrivateKey(String pemData) throws Exception {
        String key = pemData.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                            .replace("-----END RSA PRIVATE KEY-----", "")
                            .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(key);
        
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePrivate(spec);
    }
}
