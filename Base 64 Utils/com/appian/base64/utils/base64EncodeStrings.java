package com.appian.base64.utils;


import java.util.Base64;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
@Category("category.name.AppianScriptingFunctions")
public class base64EncodeStrings {
	 @Function
		public String base64EncodeString (@Parameter String text) {
		return Base64.getEncoder().encodeToString(text.getBytes());
	 }

}
