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

import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.exception.ConnectionException;
import com.telefonica.claudia.smi.utils.OneProperties;

/**
 * Unit tests for OpenNebulaConnector
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestOpenNebulaConnectorCorrect {
    
    private static final String FAKED_ACTION = "faked.action";	
	
    private XmlRpcClient mockClient;
    private ConnectionManager<Object> myConnector;
    private ArgumentCaptor<String> action;
    private ArgumentCaptor<ArrayList> params;
    private String session;
    private Object[] res;
        
    @Before
    public void setup() throws ConnectionException, IllegalArgumentException, IllegalAccessException, XmlRpcException {
    	myConnector = OpenNebulaConnector.getInstance();
    	
    	mockClient = mock(XmlRpcClient.class);
        setSuperclassPrivateField(myConnector, mockClient);
        
        action = ArgumentCaptor.forClass(String.class);
        params = ArgumentCaptor.forClass(ArrayList.class);
        
        String oneUser = PropertyManager.getInstance().getProperty(OneProperties.USER_PROPERTY);
    	String onePass = PropertyManager.getInstance().getProperty(OneProperties.PASSWORD_PROPERTY);
    	session = oneUser + ":" + onePass;
        
        res = new Object[2];
        res[0] = true;
        res[1] = "";
        
        when(mockClient.execute(anyString(), anyList())).thenReturn(res);
    }
    
	@Test
    public void shouldSendARequestWithSessionParameter() throws XmlRpcException, ConnectionException  {
		String requestRes = myConnector.sendRequest(FAKED_ACTION, new ArrayList<Object>());
        
    	verify(mockClient).execute(action.capture(), params.capture());
        assertEquals(FAKED_ACTION, action.getValue());
        assertEquals(session, params.getValue().get(0));
        assertEquals(res[1], requestRes);
    }
       
}
