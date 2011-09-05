package com.telefonica.claudia.paas.sdc;

import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.paas.vmiHandler.SDCClient;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;

public class InvokeSDCInstallProduct {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SDCClient sdc = new SDCClient("http://109.231.82.11:8080/sdc");
		Product product = new Product ("tomcat");
		product.setProductUrl("http: ");
		product.setProductVendor("vendor");
		product.setProductVersion("5.5");

		product.addProperty("propq1", "value1");
		product.addProperty("propq2", "value2");
		product.addProperty("propq3", "value3");
		try {
			sdc.installProduct(product, "109.231.82.5");
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
