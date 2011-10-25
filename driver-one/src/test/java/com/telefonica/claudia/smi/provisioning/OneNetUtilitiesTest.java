package com.telefonica.claudia.smi.provisioning;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class OneNetUtilitiesTest {

	/*		@Test
public void testTCloud2ONENet1() {
		
		OneNetUtilities utilities = new OneNetUtilities ("br0");
		
		try {
			utilities.TCloud2ONENet(readFileAsString ("./src/test/resources/network1.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTCloud2ONENet2() {
		
		OneNetUtilities utilities = new OneNetUtilities ("br0");
		
		try {
			utilities.TCloud2ONENet(readFileAsString ("./src/test/resources/network2.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Test
	public void testTCloud2ONENet3() {
		
		OneNetUtilities utilities = new OneNetUtilities ("br0");
		
		try {
			utilities.TCloud2ONENet(readFileAsString ("./src/test/resources/network3.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
