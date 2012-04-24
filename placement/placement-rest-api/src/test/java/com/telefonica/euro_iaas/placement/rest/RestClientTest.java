package com.telefonica.euro_iaas.placement.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.schemas.nuba_model.exp.ArrayCIMUserEntityType;
import com.telefonica.schemas.nuba_model.exp.CIMUserEntityType;
import com.telefonica.schemas.tcloud._1.InstantiateOvfParamsType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:restTemplateContext.xml"})
public class RestClientTest {
	
	private static final String OVF_URL_LIST = "http://127.0.0.1:8892/placement/api/org/1/vdc/1/action/ovflistprovidermapping";
	private static final String OVF_URL_BEST = "http://127.0.0.1:8892/placement/api/org/1/vdc/1/action/ovfprovidermapping";
	
	private static final String OVF_TEST = "DescriptorOVFtest.xml";
	
	private static final String TCLOUD_URL_LIST = "http://127.0.0.1:8892/placement/api/org/1/vdc/1/action/listprovidermapping";
	private static final String TCLOUD_URL_BEST = "http://127.0.0.1:8892/placement/api/org/1/vdc/1/action/providermapping";
	private static final String TCLOUD_TEST = "DemoService01.xml";


	@Autowired
	RestTemplate restTemplate;

	@Before
	public void setup(){
		
	}

	@Test
	public void testListProviderMappingJersey() throws JAXBException{
		
		Reader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			    		TCLOUD_TEST)));
		
		
	   // JAXBContext context = JAXBContext.newInstance(InstantiateOvfParamsType.class);
		JAXBContext context = JAXBContext.newInstance("com.telefonica.schemas.tcloud._1");
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    

	    
	    JAXBElement<InstantiateOvfParamsType> obj = (JAXBElement<InstantiateOvfParamsType>) unmarshaller.unmarshal( reader );
	    
	    //InstantiateOvfParamsType instantiateOvfParamsType = (InstantiateOvfParamsType) obj.getValue();		
		
		Client c = Client.create();
		WebResource r = c.resource(TCLOUD_URL_LIST);
		
		//String request = "content";
		ArrayCIMUserEntityType response = r.accept(MediaType.APPLICATION_XML_TYPE).
	        type(MediaType.APPLICATION_XML_TYPE).
	        post(ArrayCIMUserEntityType.class, obj);
	    System.out.println("response testListProviderMappingJersey->" + response);
	    System.out.println(" length: " + response.getCIMUserEntityType().size());
	    for (CIMUserEntityType user: response.getCIMUserEntityType()){
	    	System.out.println(" user: " + user.getElementName().getValue());
	    	System.out.println(" user url: " + user.getURLDRP());
	    }
	    
	}	
	
	@Test
	public void testOVFListProviderMappingJersey() throws JAXBException{
		
		Reader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			    		OVF_TEST)));
	    JAXBContext context = JAXBContext.newInstance(EnvelopeType.class);
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) unmarshaller.unmarshal( reader );
	    EnvelopeType envelope = (EnvelopeType) obj.getValue();		
		
		Client c = Client.create();
		WebResource r = c.resource(OVF_URL_LIST);
		
		String request = "content";
		ArrayCIMUserEntityType response = r.accept(MediaType.APPLICATION_XML_TYPE).
	        type(MediaType.APPLICATION_XML_TYPE).
	        post(ArrayCIMUserEntityType.class, obj);
	    System.out.println("response" + response);
	    System.out.println("length: " + response.getCIMUserEntityType().size());
	}
	
	@Test
	public void testOVFProviderMappingJersey() throws JAXBException{
		
		Reader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			    		OVF_TEST)));
	    JAXBContext context = JAXBContext.newInstance(EnvelopeType.class);
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) unmarshaller.unmarshal( reader );
	    EnvelopeType envelope = (EnvelopeType) obj.getValue();		
		
		Client c = Client.create();
		WebResource r = c.resource(OVF_URL_BEST);
		
		String request = "content";
		CIMUserEntityType response = r.accept(MediaType.APPLICATION_XML_TYPE).
	        type(MediaType.APPLICATION_XML_TYPE).
	        post(CIMUserEntityType.class, obj);
	    System.out.println("response testOVFProviderMappingJersey ->" + response + " is: " + response.getId() + " url: " + response.getURLDRP());
	}	
	
	
	/*
	@Test
	public void testListProviderMapping() throws JAXBException{
		Reader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			    		OVF_TEST)));
		
		
	    JAXBContext context = JAXBContext.newInstance(EnvelopeType.class);
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    
	    
	    JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) unmarshaller.unmarshal( reader );
	    EnvelopeType envelope = (EnvelopeType) obj.getValue();
	    //EnvelopeType envelope = (EnvelopeType)unmarshaller.unmarshal(reader);
		
		
		CloudProviderList cpl = restTemplate.postForObject(URL_LIST, envelope, CloudProviderList.class);
		System.out.println ("result: " + cpl);
		
	    String cp = restTemplate.postForObject(URL_LIST, envelope, String.class);
		System.out.println ("result in xml: " + cp);
	}
	
	@Test
	public void testProviderMapping() throws JAXBException{
		Reader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			        "DescriptorOVF.xml")));
		
		
	    JAXBContext context = JAXBContext.newInstance(EnvelopeType.class);
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    
	    JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) unmarshaller.unmarshal( reader );
	    EnvelopeType envelope = (EnvelopeType) obj.getValue();
	    //EnvelopeType envelope = (EnvelopeType)unmarshaller.unmarshal(reader);
		
		//CloudProvider cp = restTemplate.postForObject(URL_BEST, envelope, CloudProvider.class);
	    String cp = restTemplate.postForObject(URL_BEST, envelope, String.class);
		System.out.println ("result: " + cp);
	}
	*/
}
