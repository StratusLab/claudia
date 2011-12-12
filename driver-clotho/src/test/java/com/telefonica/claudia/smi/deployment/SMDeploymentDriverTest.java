package com.telefonica.claudia.smi.deployment;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.junit.Test;

public class SMDeploymentDriverTest {

	@Test
	public void testCreateVdc() {
		/*Properties props = new Properties();
		 
	    FileInputStream fis;
		try {
			fis = new FileInputStream("./src/main/resources/tcloud.properties");
			props.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		try {
			SMDeploymentDriver drier = new SMDeploymentDriver (props);
			
			drier.createVdc("org", "customer");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	}

}
