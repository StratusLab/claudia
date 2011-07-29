/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DiskUtils {
	public static String readFile(String path, String parentPath) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(parentPath, path))));
		StringBuffer ruleFile= new StringBuffer();
		String actualString;

		while ((actualString=reader.readLine())!=null) {
			ruleFile.append(actualString);
		}
		
		return ruleFile.toString();
	}
}
