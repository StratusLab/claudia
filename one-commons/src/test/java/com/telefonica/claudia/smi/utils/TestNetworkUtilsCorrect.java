/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.utils;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for NetworkUtils.
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestNetworkUtilsCorrect {
    
	@Test
    public void shouldCountBitsCorrectly() {
		short[] ip = new short[4];
		ip[0]=127;
		ip[1]=0;
		ip[2]=0;
		ip[3]=1;
				
		int bits = NetworkUtils.getBitNumber(ip);
		
		assertEquals(8, bits);
    }
       
}
