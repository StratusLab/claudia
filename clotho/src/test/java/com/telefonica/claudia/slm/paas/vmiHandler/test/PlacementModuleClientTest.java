package com.telefonica.claudia.slm.paas.vmiHandler.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.abiquo.ovf.exceptions.XMLException;
import com.telefonica.claudia.slm.paas.vmiHandler.PlacementModuleClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;

public class PlacementModuleClientTest {

	@Test
	public void testBestOVFProvider() {
		
		PlacementModuleClient placement = new PlacementModuleClient ("http://109.231.81.30:8892/placement/");
		
		try {
			placement.bestOVFProvider("./src/test/resources/DescriptorOVF.xml");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
