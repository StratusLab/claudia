package com.telefonica.claudia.slm.deployment.hwItems;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Property {
	

    	@Id
    	@GeneratedValue
    	
    	public long internalId;
    	@Basic
    	    	String key2 = null;
    	@Basic
    	String value = null;
    	
    	 public Property(String key2, String value2) {
			key2=key2;
			value=value2;
		}

		public String getKey(){
    	        return key2;
    	 }
    	    
    	 public void setKey(String key){
    	        this.key2 = key;
    	    }
    	 
    	 public void setValue(String value){
 	        this.value = value;
 	    }
    	 
    	 public String getValue(){
 	        return value;
 	 }
    	
    	 

}
