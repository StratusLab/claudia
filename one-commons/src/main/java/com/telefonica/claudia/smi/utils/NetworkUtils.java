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

/**
 * Network utilities
 * 
 * @author luismarcos.ayllon
 *
 */
public class NetworkUtils {

	/**
	 * Get the number of bits with value 1 in the given IP. 
	 * 
	 * @param ip 
	 * 		the given ip
	 * @return
	 * 		the number of bit with value 1 
	 */
	public static int getBitNumber (short[] ip) {
		
		if (ip == null || ip.length != 4)
			return 0;
		
		int bits=0;
		for (int i=0; i < 4; i++)
			for (int j=0; j< 15; j++) 
				bits += ( ((short)Math.pow(2, j))& ip[i]) / Math.pow(2, j);
			
		return bits;
	}
}
