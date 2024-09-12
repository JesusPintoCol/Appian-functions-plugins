package com.appian.pem.encryption;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;
import com.appiancorp.suiteapi.content.DocumentInputStream;
import com.appiancorp.suiteapi.content.ContentService;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
@Category("category.name.AppianScriptingFunctions")
public class pemEncryptionGenerator {
	
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
}
