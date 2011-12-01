/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.connection;


import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;
import static com.telefonica.claudia.ClothoTest.*;

import java.util.ArrayList;

import org.junit.Before;
import org.mockito.ArgumentCaptor;

import com.telefonica.claudia.smi.exception.ConnectionException;

/**
 * Unit tests for XmlRpcConnectionManager
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestXMLRPCConnectionManagerCorrect {
    
    
	private final static String FAKED_URL="http://localhost/";
	
    private ConnectionManager<Object> myConecctor;
    private XmlRpcClient mockClient;
    private ArgumentCaptor<String> action;
    private Object[] res;
        
    @Before
    public void setup() throws ConnectionException, IllegalArgumentException, IllegalAccessException, XmlRpcException {
    	myConecctor = new XmlRpcConnectionManager(FAKED_URL);
    	
    	mockClient= mock(XmlRpcClient.class);
        setPrivateField(myConecctor, mockClient);
      
        action = ArgumentCaptor.forClass(String.class);
        
        res = new Object[2];
        res[0] = true;
        res[1] = "";
        
        when(mockClient.execute(anyString(), anyList())).thenReturn(res);
        
    }
    
	@Test
    public void shouldSendARequestWithValidParameters() throws XmlRpcException, ConnectionException  {
		String myAction = "an.action";
		
		String requestRes = myConecctor.sendRequest(myAction, new ArrayList<Object>());
        
    	verify(mockClient).execute(action.capture(), anyList());
        assertEquals(myAction, action.getValue());
        assertEquals(res[1], requestRes);
    }
       
}
