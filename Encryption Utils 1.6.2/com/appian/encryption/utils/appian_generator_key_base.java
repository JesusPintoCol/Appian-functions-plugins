package com.appian.encryption.utils;
import java.util.Base64;

import java.security.NoSuchAlgorithmException;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;




import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;



@Category("category.name.AppianScriptingFunctions")
public class appian_generator_key_base {
	@Function
    public String getkeyaes256base(){
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
}
