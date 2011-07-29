package com.telefonica.claudia.rules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ActionsPlugin {
	
	/**
	 * The Actions object will be instantiated and made available in the RIF 
	 * rules with the name returned from this method.
	 * 
	 * @return
	 */
	public String variableName();
}
