package com.appian.base64.utils;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class base64DecodeStrings {
	
	
		 @Function
			public String base64DecodeString (@Parameter String text) {
			 byte[] combined = Base64.getDecoder().decode(text);
		     return new String(combined, StandardCharsets.UTF_8);
			
		 }

	}

