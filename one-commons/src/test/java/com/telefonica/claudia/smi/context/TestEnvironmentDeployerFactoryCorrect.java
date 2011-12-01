/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.context;

import static com.telefonica.claudia.ClothoTest.setPrivateField;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.claudia.smi.context.impl.EnvironmentDeployerFactoryImpl;
import com.telefonica.claudia.smi.exception.ConnectionException;

/**
 * Unit tests for EnvironmentDeployerFactory
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestEnvironmentDeployerFactoryCorrect {

	private EnvironmentDeployerFactory myFactory;
	private EnvironmentDeployer mockDeployer;
	
	@Before
    public void setup() throws ConnectionException, IllegalArgumentException, IllegalAccessException, XmlRpcException {
		myFactory = new EnvironmentDeployerFactoryImpl();
    	
		mockDeployer = mock(EnvironmentDeployer.class);
        setPrivateField(myFactory, mockDeployer);
    }
    
	@Test
    public void shouldReturnAEnvironmentDeployer() throws XmlRpcException, ConnectionException  {
		EnvironmentDeployer res = myFactory.getInstance();
        
        assertEquals(res, mockDeployer);
    }
	
}
