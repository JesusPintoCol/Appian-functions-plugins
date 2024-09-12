package com.appian.hello;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Category;


@Category("category.name.AppianScriptingFunctions")

public class helloWorld {
	@Function
	public String hello(){
		 String base64Key ="hola mundo";
		 return base64Key;
	}
	
}
