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
