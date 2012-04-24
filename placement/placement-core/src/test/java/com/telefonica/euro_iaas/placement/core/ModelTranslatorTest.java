package com.telefonica.euro_iaas.placement.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.placement.model.application.ServiceApplication;
import com.telefonica.euro_iaas.placement.model.application.VDC;
import com.telefonica.euro_iaas.placement.model.application.VEERequired;
import com.telefonica.schemas.tcloud._1.InstantiateOvfParamsType;

public class ModelTranslatorTest {

	ModelTranslator modelTranslator;
	
	@Before
	public void setUp() {
		modelTranslator = new ModelTranslator();
	}
	
	@Test
	public void testNotMatch() throws JAXBException {
	       JAXBContext jc = JAXBContext.newInstance( "org.dmtf.schemas.ovf.envelope._1" );
	       Unmarshaller u = jc.createUnmarshaller();
	       JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) u.unmarshal(  getClass().getClassLoader().getResourceAsStream("DescriptorOVF.xml") );
	       EnvelopeType e = (EnvelopeType) obj.getValue();
	    
	       VDC vdc = modelTranslator.getVDC(e);
	       Assert.assertNotNull(vdc);
	       System.out.println(vdc);
	}
	
	@Test
	public void testRubiOVF() throws JAXBException {
	       JAXBContext jc = JAXBContext.newInstance( "org.dmtf.schemas.ovf.envelope._1" );
	       Unmarshaller u = jc.createUnmarshaller();
	       
	       JAXBElement<EnvelopeType> obj = (JAXBElement<EnvelopeType>) u.unmarshal(  getClass().getClassLoader().getResourceAsStream("rubisOVF.xml") );
	       EnvelopeType e = (EnvelopeType) obj.getValue();	    

	       VDC vdc = modelTranslator.getVDC(e);
	       
	       Assert.assertNotNull(vdc);
	       Assert.assertNotNull(vdc.getServiceApplications());
	       Assert.assertEquals(1, vdc.getServiceApplications().size());
	       ServiceApplication sa = vdc.getServiceApplications().iterator().next();
	       Assert.assertNotNull(sa.getVeesRequired());
	       Assert.assertEquals(6, sa.getVeesRequired().size());
	}
	
	@Test
	public void testDemoService() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("com.telefonica.schemas.tcloud._1");
	    Unmarshaller u = jc.createUnmarshaller();
	    
	    JAXBElement<InstantiateOvfParamsType> obj = (JAXBElement<InstantiateOvfParamsType>) u.unmarshal( getClass().getClassLoader().getResourceAsStream("DemoService01.xml") );
	    InstantiateOvfParamsType instantiateOvfParamsType = (InstantiateOvfParamsType) obj.getValue();	
	    
	    EnvelopeType e = instantiateOvfParamsType.getEnvelope();
	    
	    VDC vdc = modelTranslator.getVDC(e);
	  
	    Assert.assertNotNull(vdc);
	    Assert.assertNotNull(vdc.getServiceApplications());
	    Assert.assertEquals(1, vdc.getServiceApplications().size());
	    ServiceApplication sa = vdc.getServiceApplications().iterator().next();
	    Assert.assertNotNull(sa.getVeesRequired());
	    Assert.assertEquals(2, sa.getVeesRequired().size());	    
	    
	    for (VEERequired ve : sa.getVeesRequired()){
	    	System.out.println ("vee: " + ve);
	    }

	}
}
