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

import com.telefonica.claudia.smi.context.impl.EnvironmentCreatorFactoryImpl;
import com.telefonica.claudia.smi.exception.ConnectionException;

/**
 * Unit tests for EnvironmentCreatorFactory
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestEnvironmentCreatorFactoryCorrect {

	private EnvironmentCreatorFactory myFactory;
	private EnvironmentCreator mockCreator;
	
	@Before
    public void setup() throws ConnectionException, IllegalArgumentException, IllegalAccessException, XmlRpcException {
		myFactory = new EnvironmentCreatorFactoryImpl();
    	
		mockCreator = mock(EnvironmentCreator.class);
        setPrivateField(myFactory, mockCreator);
    }
    
	@Test
    public void shouldReturnAEnvironmentCreator() throws XmlRpcException, ConnectionException  {
		EnvironmentCreator res = myFactory.getInstance();
        
        assertEquals(res, mockCreator);
    }
	
}
