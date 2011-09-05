package com.telefonica.claudia.paas.sdc;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.paas.vmiHandler.SDCClient;

public class CreateXMLProduct {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		Product product = new Product ("tomcat");
		product.setUrl("http: ");
		product.setVendor("vendor");
		product.setVersion("5.5");

		product.addProperty("propq1", "value1");
		product.addProperty("propq2", "value2");
		product.addProperty("propq3", "value3");
		SDCClient sdc = new SDCClient ("http://dddd:8989/dd");
		Document doc = null;
		try {
			doc = sdc.getInstallProductParams (product,"10.87.67.54");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println (PaasUtils.tooString(doc));

	}

}
