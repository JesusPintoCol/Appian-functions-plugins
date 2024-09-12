package com.appian.key.generator;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Category;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
@Category("category.name.AppianScriptingFunctions")
public class keyGenerator {
	@Function
    public String aeskey(){
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.init(256); // Tamaño de clave de 256 bits
        SecretKey key = keyGen.generateKey();

        // Convertir la clave a Base64
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        
        return base64Key;
    }
}
