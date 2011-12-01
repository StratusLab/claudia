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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.Envelope;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.OVFEnvironmentUtils;
import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;
import com.telefonica.claudia.smi.context.Environment;
import com.telefonica.claudia.smi.context.EnvironmentCreator;
import com.telefonica.claudia.smi.exception.EnvironmentCreationException;
import com.telefonica.claudia.smi.exception.ParserException;
import com.telefonica.claudia.smi.utils.OneProperties;
import com.telefonica.claudia.smi.utils.XMLUtils;

/**
 * Creates the virtual machine environment using OVF information
 * 
 * @author luismarcos.ayllon
 *
 */
public class EnvironmentCreatorImpl implements EnvironmentCreator {

	private static Logger log = Logger.getLogger(EnvironmentCreatorImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Environment create(String ovf) throws EnvironmentCreationException {
		EnvironmentImpl env = new EnvironmentImpl();
		Document doc = null;

		try {
			doc = XMLUtils.parse(ovf, true);
		} catch (ParserException e) {
			throw new EnvironmentCreationException (e);
		}
		
		if (!doc.getFirstChild().getNodeName().equals(TCloudConstants.TAG_INSTANTIATE_OVF)) {
			log.error("Element <"+TCloudConstants.TAG_INSTANTIATE_OVF+"> not found.");
			throw new EnvironmentCreationException ("Element <"+TCloudConstants.TAG_INSTANTIATE_OVF+"> not found.");
		}
		
		/* Gets the replica's FQN */
		Element root = (Element) doc.getFirstChild();
		String name = root.getAttribute("name");
		env.setVmFqn(name);
		
		Envelope envelope = null;
		ContentType entityInstance = null;
		
		/* Replaces the environment macros in the OVF */
		try {
			envelope = getEnvelope(doc);
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
			
			if (entityInstance instanceof VirtualSystemType)  {
				envelope = replaceEnvironmentMacros(envelope, 
						Integer.parseInt(URICreation.getReplica(name)), 
						URICreation.getOrg(name), 
						URICreation.getService(name), 
						PropertyManager.getInstance().getProperty(OneProperties.MONITORING_CHANNEL), 
						null, 
						null, 
						null, 
						null, 
						null);
			}
		} catch (IllegalArgumentException iae) {
			log.error("Some data could not be calculated for the OVF environment: " + iae.getMessage());
			throw new EnvironmentCreationException(iae);
		} catch (EmptyEnvelopeException e) {
			log.error("Empty envelope found: " + e);
			throw new EnvironmentCreationException("Empty envelope found: " + e.getMessage());
		}
		
		/* Creates the environment content */
		try {
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
			VirtualSystemType vs = (VirtualSystemType) entityInstance;
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			OVFEnvironmentUtils.createOVFEnvironment(vs,
					0,
					"",
					"",
					"", 
					null,
					null,
					null,
					null,
					null,
					output,
					true);
			
			log.debug("OVF Environment file for VM [" +  output.toString()+ "]");
			env.setContent(output.toString());
			return env;
			
		} catch (EmptyEnvelopeException e) {
			log.error("Environment could not be created. Empty envelope found: " + e);
			throw new IllegalArgumentException("Empty envelope found: " + e.getMessage());
		} catch (IPNotFoundException e) {
			log.error("Environment could not be created. No IP found: " + e);
			throw new IllegalArgumentException("No IP found: " + e.getMessage());
		} catch (DNSServerNotFoundException e) {
			log.error("Environment could not be created. No DNS found: " + e);
			throw new IllegalArgumentException("No DNS found: " + e.getMessage());
		} catch (NetmaskNotFoundException e) {
			log.error("Environment could not be created. No Netmask found: " + e);
			throw new IllegalArgumentException("No Netmask found: " + e.getMessage());
		} catch (GatewayNotFoundException e) {
			log.error("Environment could not be created. No Gateway found: " + e);
			throw new IllegalArgumentException("No Gateway found: " + e.getMessage());
		} catch (PrecedentTierEntryPointNotFoundException e) {
			log.error("Environment could not be created. No Precedent Tier found: " + e);
			throw new IllegalArgumentException("No Precedent Tier found: " + e.getMessage());
		} catch (NotEnoughIPsInPoolException e) {
			log.error("Environment could not be created. Not enough IPs found: " + e);
			throw new IllegalArgumentException("Not enough IPs found: " + e.getMessage());
		} catch (PoolNameNotFoundException e) {
			log.error("Environment could not be created. No Pool Name found: " + e);
			throw new IllegalArgumentException("Pool Name not found: " + e.getMessage());
		}
			
	}
		

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Environment create(String fqn, String text) throws EnvironmentCreationException {
		EnvironmentImpl env = new EnvironmentImpl();
		env.setVmFqn(fqn);
		env.setContent(text);
		
		return env;
	}
	
	/**
	 * Performs a macro replacement in the ProductSections of the Envelope passed as argument 
	 * in the context of the VirtualSystem.
	 * 
	 * @param ovf
	 * @param instanceNumber
	 * @param domain
	 * @param serviceId
	 * @param monitoringChannel
	 * @param ips
	 * @param netmasks
	 * @param dnsServers
	 * @param gateways
	 * @param entryPoints
	 * @return 
	 * 		the envelope element with all macros replaced.
	 * @throws EnvironmentCreationException 
	 */
	private Envelope replaceEnvironmentMacros (Envelope envelope,  
			int instanceNumber, 
			String domain, 
			String serviceId, 
			String monitoringChannel, 
			HashMap<String,ArrayList<String>> ips, 
			HashMap<String, String> netmasks, 
			HashMap<String, String> dnsServers, 
			HashMap<String, String> gateways,
			HashMap<String, HashMap<String, String> > entryPoints) throws EnvironmentCreationException {
			
		try {
			ContentType entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);

			if (entityInstance instanceof VirtualSystemType) {
				OVFEnvelopeUtils.inEnvolopeMacroReplacement(envelope, 
						(VirtualSystemType) entityInstance, 
						instanceNumber, 
						domain, 
						serviceId, 
						monitoringChannel, 
						ips,
						netmasks, 
						dnsServers, 
						gateways, 
						entryPoints);	
				return envelope;
			} else {
				throw new IllegalArgumentException("Virtual System not found.");
			}
		} catch (EmptyEnvelopeException e) {
			log.error("Macros could not be replaced. Empty envelope found: " + e.getMessage());
			throw new IllegalArgumentException("Empty envelope found: " + e.getMessage());
		} catch (IPNotFoundException e) {
			log.error("Macros with IPs not supported:" + e.getMessage());
			throw new IllegalArgumentException("No IP found: " + e.getMessage());
		} catch (DNSServerNotFoundException e) {
			log.error("Macros with IPs not supported:" + e.getMessage());
			throw new IllegalArgumentException("No DNS found: " + e.getMessage());
		} catch (NetmaskNotFoundException e) {
			log.error("Macros with IPs not supported:" + e.getMessage());
			throw new IllegalArgumentException("No Netmask found: " + e.getMessage());
		} catch (GatewayNotFoundException e) {
			log.error("Macros with IPs not supported:" + e.getMessage());
			throw new IllegalArgumentException("No Gateway found: " + e.getMessage());
		} catch (PrecedentTierEntryPointNotFoundException e) {
			throw new IllegalArgumentException("No Precedent Tier found: " + e.getMessage());
		} catch (NotEnoughIPsInPoolException e) {
			log.error("Macros with IPs not supported");
			throw new IllegalArgumentException("Not enough IPs found: " + e.getMessage());
		} catch (PoolNameNotFoundException e) {
			throw new IllegalArgumentException("Pool Name not found: " + e.getMessage());
		}
	}
	
	/**
	 * Extracts the envelope element from the given document
	 * 
	 * @param doc
	 * @return
	 * @throws EnvironmentCreationException
	 */
	private Envelope getEnvelope (Document doc) throws EnvironmentCreationException{
		NodeList envelopeItems = doc.getElementsByTagNameNS("*", TCloudConstants.TAG_ENVELOPE);
		
		if (envelopeItems.getLength() != 1) {
			log.error("Envelope items not found.");
			throw new EnvironmentCreationException ("Envelope items not found.");
		}
		
		Envelope envelope = null;
		try {
			Document ovfDoc = XMLUtils.createDocument(true);
			ovfDoc.appendChild(ovfDoc.importNode(envelopeItems.item(0), true));
			
			OVFSerializer ovfSerializer = OVFSerializer.getInstance();
			ovfSerializer.setValidateXML(false);
			
			envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(DataTypesUtils.serializeXML(ovfDoc).getBytes()));
			return envelope;
		} catch (JAXBException e) {
			log.error("Envelope can not be extracted");
			throw new EnvironmentCreationException (e);
		} catch (XMLException e) {
			log.error("Envelope can not be extracted");
			throw new EnvironmentCreationException (e);
		} catch (ParserException e) {
			log.error("Envelope can not be extracted");
			throw new EnvironmentCreationException (e);
		}
		
	}
		
}
	
