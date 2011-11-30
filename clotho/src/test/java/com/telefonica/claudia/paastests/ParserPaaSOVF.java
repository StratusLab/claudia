package com.telefonica.claudia.paastests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.OperatingSystemSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.smi.DataTypesUtils;

public class ParserPaaSOVF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String ovf = "src/test/resources/testpaas.xml";
	
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		try {
			ovfSerializer.setValidateXML(false);
		} catch (JAXBException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		EnvelopeType envelope = null;
		try {
			envelope = ovfSerializer.readXMLEnvelope(new FileInputStream(ovf));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XMLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ContentType entityInstance = null;
		try {
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
		} catch (EmptyEnvelopeException e) {
			e.printStackTrace();
		}
		if (entityInstance instanceof VirtualSystemType) {

			//TODO

		} else if (entityInstance instanceof VirtualSystemCollectionType) {
			VirtualSystemCollectionType virtualSystemCollectionType = (VirtualSystemCollectionType) entityInstance;
			Map<String, String> balancedVEEs = new HashMap<String, String>();

			for (VirtualSystemType vs : OVFEnvelopeUtils.getVirtualSystems(virtualSystemCollectionType)) {

				OperatingSystemSectionType vh = null;;
				try {
					vh = OVFEnvelopeUtils.getSection(vs, OperatingSystemSectionType.class);
					System.out.println (vh.getDescription().getValue());
				} catch (SectionNotPresentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (InvalidSectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		}
		
		
	
		


	}

}
