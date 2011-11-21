package com.telefonica.claudia.driver_mon;

import java.util.Properties;

public class DBUtils {
	
    private static final String DATABASE_URL = "drivers.database.url";
    private static final String DATABASE_USER = "drivers.database.user"; 
    private static final String DATABASE_PASSWORD = "drivers.database.password"; 
    
    public static String getUser(Properties prop) {
    	return prop.getProperty(DATABASE_USER);
    }
    
    public static String getPassword(Properties prop) {
    	return prop.getProperty(DATABASE_PASSWORD);
    }
    
    public static String getURL(Properties prop) {
    	return prop.getProperty(DATABASE_URL);
    }
}
