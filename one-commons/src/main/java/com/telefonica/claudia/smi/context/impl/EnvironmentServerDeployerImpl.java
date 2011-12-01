/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.context.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.context.Environment;
import com.telefonica.claudia.smi.context.EnvironmentDeployer;
import com.telefonica.claudia.smi.exception.EnvironmentDeploymentException;
import com.telefonica.claudia.smi.utils.Constants;
import com.telefonica.claudia.smi.utils.OneProperties;

/**
 * An environment deployer implementation to deploy the environment 
 * using REST services
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentServerDeployerImpl implements EnvironmentDeployer {

	private static Logger log = Logger.getLogger(EnvironmentServerDeployerImpl.class);
	
	private String host;
	private String port;
	private String path;
	private HttpClient httpClient;
	
	/**
	 * Constructor
	 */
	public EnvironmentServerDeployerImpl (){
		host = PropertyManager.getInstance().getProperty(OneProperties.CONTEXT_HOST_PROPERTY);
		port = PropertyManager.getInstance().getProperty(OneProperties.CONTEXT_PORT_PROPERTY);
		path = Constants.VM_URL;
		httpClient = new DefaultHttpClient(); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deploy(Environment environment) throws EnvironmentDeploymentException {
		String vm_url = Constants.PROTOCOL + host + ":" + port + path + "/" + environment.getVmFqn();
		HttpRequestBase method = new HttpPost(vm_url);
        
        try {
        	HttpEntity request = new StringEntity(environment.getContent());
          
        	((HttpPost) method).setEntity(request);
            method.setHeader("Content-Type", "application/xml");
            
            HttpResponse response = httpClient.execute(method);
            if (response.getStatusLine().getStatusCode() != 200)
            	throw new EnvironmentDeploymentException("Remote HTTP Error code: " + response.getStatusLine().getStatusCode());

            log.info("Deploying context in " + vm_url);
		} catch (UnsupportedEncodingException e) {
			log.error (e.getMessage());
			throw new EnvironmentDeploymentException (e);
		} catch (ClientProtocolException e) {
			log.error (e.getMessage());
			throw new EnvironmentDeploymentException (e);
		} catch (IOException e) {
			log.error (e.getMessage());
			throw new EnvironmentDeploymentException (e);
		}

	}

}
