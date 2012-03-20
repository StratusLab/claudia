/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as
* published by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.vmiHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.paas.OVFContextualization;
import com.telefonica.claudia.slm.paas.PaasUtils;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.NotEnoughResourcesException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.VEEReplicaDescriptionMalformedException;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

//import es.test.GetVEE;

public class TCloudClient implements VMIHandler {

    private static final long POLLING_INTERVAL = 15000;
    private Client client;
    private String serverURL;
    


    private static Logger logger = Logger.getLogger(TCloudClient.class);

    public TCloudClient(String url) {
        if (url.charAt(url.length()-1) == '/')
            serverURL = url.substring(0, url.length()-1);
        else
            serverURL = url;

        client = new Client(Protocol.HTTP);
    }


    public Document getTCloudNetworkParameters(Network networkConf) throws ParserConfigurationException {

    	logger.info("Obtain XML for network with IP management");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
       
        Element root = doc.createElement(TCloudConstants.TAG_NETWORK_ROOT);
        root.setAttribute(TCloudConstants.ATTR_NETWORK_NAME, networkConf.getFQN().toString());
        doc.appendChild(root);

        Element baseAddress = doc.createElement(TCloudConstants.TAG_NETWORK_BASE_ADDRESS);
        baseAddress.appendChild(doc.createTextNode(networkConf.getNetworkAddresses()[0]));
        root.appendChild(baseAddress);

        Element netmask = doc.createElement(TCloudConstants.TAG_NETWORK_NETMASK);
        netmask.appendChild(doc.createTextNode(networkConf.getNetworkAddresses()[1]));
        root.appendChild(netmask);

        String macEnable = null;
        if (SMConfiguration.getInstance()!=null)
          macEnable = SMConfiguration.getInstance().getNetworkMacEnable();

        if (macEnable!=null && macEnable.equals("true")){


        logger.info("PONG Private: " +networkConf.getPrivateNet());
        Boolean nettype=networkConf.getPrivateNet();
        


        if (nettype==false){

           String[] networkAddresses = SMConfiguration.getInstance().getNetworkRanges();
            String range = networkAddresses[0];
            String[] networkMacAddresses = SMConfiguration.getInstance().getNetworkMacList();
            String mac = networkMacAddresses[0];
            logger.info("PONG MAC section: " + mac);


            int IPPos= range.indexOf("IP:");
            String ip;
            if (IPPos>0) {
                ip = range.substring(IPPos+3, range.indexOf(";", IPPos)).trim();
                logger.info("PONG IP section: " + ip);
                if (ip.contains("/")) {
                    String[] ips = ip.split("/");
                    String[] macs = mac.split("/");
                    int i = 0;
                    for (String ipLease: ips){
                        Element ipleases = doc.createElement(TCloudConstants.TAG_NETWORK_IPLEASES);
                        //    ipleases.appendChild(doc.createTextNode("prueba"));

                        Element ipelem = doc.createElement(TCloudConstants.TAG_NETWORK_IP);
                        ipelem.appendChild(doc.createTextNode(ipLease));
                        ipleases.appendChild(ipelem);
                    //    logger.info("PONG IP: " + ipLease);
                        String macLease = macs[i];
                        Element macelem = doc.createElement(TCloudConstants.TAG_NETWORK_MAC);
                        macelem.appendChild(doc.createTextNode(macLease));
                        ipleases.appendChild(macelem);
                    //    logger.info("PONG MAC: " + macLease);
                        i++;
                         root.appendChild(ipleases);
                    }
                }
            }

        }
        else {
            macEnable="false";
        }

        }

        Element macenabled = doc.createElement(TCloudConstants.TAG_NETWORK_MAC_ENABLED);
        macenabled.appendChild(doc.createTextNode(macEnable));
        root.appendChild(macenabled);

        return doc;

    }
    
    public Document getTCloudNetworkParametersNotIpManagement(Network networkConf) throws Exception {

    //	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
     //   Document doc = builder.newDocument();
       
     /*   DocumentBuilderFactory factory          = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        Document doc = impl.createDocument("http://nuba.morfeo-project.org/network",TCloudConstants.TAG_NETWORK_ROOT, null);*/
       
        DOMImplementation domImpl = getDomImplementation(); 
        
        Document document = domImpl.createDocument("http://nuba.morfeo-project.org/network",
                "ns1:"+TCloudConstants.TAG_NETWORK_ROOT, null);
        
              
        document.getDocumentElement().setAttributeNS("http://nuba.morfeo-project.org/network","ns1:"+TCloudConstants.ATTR_NETWORK_NAME, networkConf.getName());

    	
    	if (networkConf.getPrivateNet())
    	{
    		logger.info("Obtain XML for network without IP management private network");  
    		
    		  Element baseAddress = document.createElementNS("http://nuba.morfeo-project.org/network","ns1:"+TCloudConstants.TAG_NETWORK_BASE_ADDRESS);
    	      baseAddress.appendChild(document.createTextNode("192.168."+SMConfiguration.getInstance().getNetworkId()+".0")); 
    	     
    	      SMConfiguration.getInstance().setNetworkId (SMConfiguration.getInstance().getNetworkId()+1);
    	      document.getDocumentElement().appendChild(baseAddress);
    	      Element netmask = document.createElementNS("http://nuba.morfeo-project.org/network","ns1:"+TCloudConstants.TAG_NETWORK_NETMASK);
    	      netmask.appendChild(document.createTextNode("255.255.255.0"));
    	      document.getDocumentElement().appendChild(netmask);  
    	}
    	else
    	{
    		logger.info("Obtain XML for network without IP management public network");  
    	}
    	
    	  OutputFormat format    = new OutputFormat (document); 
          // as a String
          StringWriter stringOut = new StringWriter ();    
          XMLSerializer serial   = new XMLSerializer (stringOut, 
                                                      format);
          try {
  			serial.serialize(document);
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
          // Display the XML
          System.out.println("XML " + stringOut.toString());
          logger.info("XML " + stringOut.toString());
          
    	this.serialize(domImpl, document);
    	 
//
//        Element gateway = doc.createElement(TCloudConstants.TAG_NETWORK_GATEWAY);
//        gateway.appendChild(doc.createTextNode(gw));
//        root.appendChild(gateway);
//         
//
//        Element dnss = doc.createElement(TCloudConstants.TAG_NETWORK_DNS);
//        dnss.appendChild(doc.createTextNode(dns));
//        root.appendChild(dnss);
//        
        
  /*      Element baseAddress = doc.createElement(TCloudConstants.TAG_NETWORK_BASE_ADDRESS);
        baseAddress.appendChild(doc.createTextNode(networkConf.getNetworkAddresses()[0]));
        root.appendChild(baseAddress);

        Element netmask = doc.createElement(TCloudConstants.TAG_NETWORK_NETMASK);
        netmask.appendChild(doc.createTextNode(networkConf.getNetworkAddresses()[1]));
        root.appendChild(netmask);*/
        


        return document;

    }

    public void allocateNetwork(Set<Network> conf)
            throws AccessDeniedException, CommunicationErrorException, NotEnoughResourcesException {
        if(conf == null)
            throw new IllegalArgumentException("Set of netowrks configurations to allocate cannot be null");

        Map<Network, String> urlTasks = new HashMap<Network, String> ();
        
        boolean management = true;
        
        management = SMConfiguration.getInstance().getIpManagement();
        
        System.out.println ("management " + management);

        for (Network networkConf: conf) {

            // Compose the URL for the network.
        	if (networkConf.getName().endsWith("public"))
        	{
        		continue;
        	}
            Reference urlNetwork = new Reference(serverURL + URICreation.getURINetAdd(networkConf.getFQN().toString()));
            logger.info (" Url network " + urlNetwork);

            // Create de DOM Representation for the network configuration
            DomRepresentation data;
            try {
            	if (management)
                    data = new DomRepresentation(MediaType.APPLICATION_XML, getTCloudNetworkParameters(networkConf));
            	else
            		data = new DomRepresentation(MediaType.APPLICATION_XML, getTCloudNetworkParametersNotIpManagement(networkConf));

            } catch (ParserConfigurationException e) {
                logger.error("Error creating parser: " + e.getMessage());
                return;
            } catch (Exception e) {
				// TODO Auto-generated catch block
            	logger.error("Error creating parser: " + e.getMessage());
                return;
			}

            // Call the server with the URI and the data
            logger.debug("Posting network allocation request for network: " + networkConf);
            Response response = client.post(urlNetwork, data);

            // Depending on the response code, return with an error, or wait for a response
            switch (response.getStatus().getCode()) {

                case 401:    // Unauthorized
                case 403:    // Forbidden
                    logger.error("Not enough privileges to access the network information.");
                    throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

                case 400:    // Bad Request
                case 404:    // Not found
                    logger.error("The resource was not found on the server; the TCloud server may be misconfigured, or the URL may be wrong.");
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

                case 501:
                case 500:
                    logger.error("Internal error in the TCloud server: " + response.getStatus().getDescription());
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


                case 202:
                case 201:
                case 200:

                    Document responseXml;

                    try {
                        responseXml = response.getEntityAsDom().getDocument();

                        
                        this.serialize(this.getDomImplementation(), responseXml);
                        NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);
                        
                        if (tasks.getLength()==0)
                        {
                        	tasks = responseXml.getElementsByTagName("ns24:Task");
                        }

                        if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("href")!= null) {
                            urlTasks.put(networkConf, ((Element)tasks.item(0)).getAttribute("href"));
                        } else {
                            throw new CommunicationErrorException("Error retrieving task URL.", new Exception(response.getStatus().getName()));
                        }

                        logger.info("Network " + networkConf + "successfully allocated.");
                    } catch (IOException e) {
                        logger.error("Error parsing the response from the server: " + e.getMessage());
                    } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }

        // Wait for the replicas to be returned.
        int finishedTasks=0;
        while (conf.size() < finishedTasks) {
            for (Network networkConf: conf) {
                String status = getTaskStatus(urlTasks.get(networkConf));

                if (status.toLowerCase().equals("success")) {
                    finishedTasks++;
                } else if (status.toLowerCase().equals("error")) {
                    throw new NotEnoughResourcesException("Network " + networkConf.getFQN()+ " could not be created", null);
                }
            }

            try {
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {}
        }
    }
    
    private Document getFictiousService (VEEReplica actualReplica)
    {
    	
    	
    	DOMImplementation domImpl = null;
		try {
			domImpl = getDomImplementation();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("Error retrieving OVF from the data model: " + e1.getMessage());
		} 
        
   
        Document doc = domImpl.createDocument("http://schemas.telefonica.com/tcloud/1.0",
                TCloudConstants.TAG_INSTANTIATE_OVF, null);
        
        try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	Element root = doc.getDocumentElement();
             	
       	root.setAttribute(
       			"xmlns",
       			"http://schemas.telefonica.com/tcloud/1.0");
           
         root.setAttribute(TCloudConstants.ATTR_INSTANTIATION_PARAMS_NAME, String.valueOf(actualReplica.getVEE().getServiceApplication().getSerAppName()));
        
         Element envelope = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1", "ns2:Envelope");
         
         Element virtualSystemCollection = doc.createElementNS("http://schemas.dmtf.org/ovf/envelope/1", "ns2:VirtualSystemCollection");
         
         envelope.appendChild(virtualSystemCollection);
         
         root.appendChild(envelope);
         
         OutputFormat format    = new OutputFormat (doc); 
         // as a String
         StringWriter stringOut = new StringWriter ();    
         XMLSerializer serial   = new XMLSerializer (stringOut, 
                                                     format);
         try {
 			serial.serialize(doc);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
         // Display the XML
         System.out.println("XML " + stringOut.toString());
         logger.info("XML " + stringOut.toString());
         return doc;
        
    	
    }

    private Document getTCloudVMParameters(VEEReplica actualReplica) throws ParserConfigurationException {

        
        DOMImplementation domImpl = null;
		try {
			domImpl = getDomImplementation();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("Error retrieving OVF from the data model: " + e1.getMessage());
		} 
        
       // Document doc = domImpl.createDocument("http://schemas.telefonica.com/tcloud/1.0",
        //        "ns0:"+TCloudConstants.TAG_INSTANTIATE_OVF, null);
        
        Document doc = domImpl.createDocument("http://schemas.telefonica.com/tcloud/1.0",
                TCloudConstants.TAG_INSTANTIATE_OVF, null);
        
     //   Element root = doc.createElement(TCloudConstants.TAG_INSTANTIATE_OVF);
        
              
     //   doc.getDocumentElement().setAttributeNS("http://nuba.morfeo-project.org/network","ns1:"+TCloudConstants.ATTR_NETWORK_NAME, networkConf.getName());

    	
    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
     //   Document doc = builder.newDocument();

    	Element root = doc.getDocumentElement();
      //  doc.appendChild(root);
    	
    	root.setAttribute(
    			"xmlns",
    			"http://schemas.telefonica.com/tcloud/1.0");
        
       // root.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.ATTR_INSTANTIATION_PARAMS_NAME, String.valueOf(actualReplica.getFQN().toString()));
        root.setAttribute(TCloudConstants.ATTR_INSTANTIATION_PARAMS_NAME, String.valueOf(actualReplica.getFQN().toString()));

        
        OVFContextualization context = new OVFContextualization();

		System.out.println();
		
		String ovfrepresentation = context.updateOvfRepresentation (actualReplica);

		
        // Add the ovf envelope to the root.
      //  if (actualReplica.getOvfRepresentation()!=null && !actualReplica.getOvfRepresentation().equals("")) {
		if (ovfrepresentation!=null && !ovfrepresentation.equals("")) {
            try {
                Document ovfEnvelope = builder.parse(new ByteArrayInputStream(ovfrepresentation.getBytes()));

                root.appendChild(doc.importNode(ovfEnvelope.getFirstChild(), true));

            } catch (SAXException e) {
                logger.error("Error parsing the OVF for replica generation: " + e.getMessage());
            } catch (IOException e) {
                logger.error("Error retrieving OVF from the data model: " + e.getMessage());
            }
        }

			   
   //     Element instantiationParams  = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_INSTANTIATION_PARAMS);
        Element instantiationParams  = doc.createElement(TCloudConstants.TAG_INSTANTIATION_PARAMS);
        root.appendChild(instantiationParams);

        //Element netConfigSection = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_NETWORK_CONFIG_SECTION);
        Element netConfigSection = doc.createElement(TCloudConstants.TAG_NETWORK_CONFIG_SECTION);
        instantiationParams.appendChild(netConfigSection);

       // Element netConfig = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_NETWORK_CONFIG);
       
        
        for (NIC nicItr: actualReplica.getNICs()) {
        	 System.out.println ("NICsss " + nicItr.getNICConf().getNetwork().getName());
        	  
        	 Element netConfig = doc.createElement(TCloudConstants.TAG_NETWORK_CONFIG);
             netConfigSection.appendChild(netConfig);  
             netConfig.setAttribute("name", nicItr.getNICConf().getNetwork().getName());
             
             
           //  <NetworkAssociation name="bsc.vdc.test.net.net1"
            //     href="http://localhost:8080/fco/api/org/bsc/vdc/test/net/net1" />
             
             //  Element netConfigAssociation = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_NETWORK_ASSOCIATION);
               Element netConfigAssociation = doc.createElement(TCloudConstants.TAG_NETWORK_ASSOCIATION);
               netConfig.appendChild(netConfigAssociation);
              // netConfigSection.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.ATTR_NETWORK_ASSOCIATION_HREF, serverURL + URICreation.getURINet(nicItr.getNICConf().getNetwork().getFQN().toString()));
               netConfigAssociation.setAttribute(TCloudConstants.ATTR_NETWORK_ASSOCIATION_HREF, serverURL + URICreation.getURINet(nicItr.getNICConf().getNetwork().getFQN().toString()));
               netConfigAssociation.setAttribute("name", nicItr.getNICConf().getNetwork().getName());
  
            }
        
        
        
     /*   <NetworkConfigSection>
        <ovf:Info>Logical networks configuration</ovf:Info>

        <NetworkConfig name="Network 1">
         <NetworkAssociation name="bsc.vdc.test.net.net1"
          href="http://localhost:8080/fco/api/org/bsc/vdc/test/net/net1" />
        </NetworkConfig>*/

        
      //  Element aspectsSection = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_ASPECTS_SECTION);
        Element aspectsSection = doc.createElement(TCloudConstants.TAG_ASPECTS_SECTION);
        instantiationParams.appendChild(aspectsSection);

       // Element aspect = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_ASPECT);
       // aspect.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+"required", "true");
       // aspect.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.ATTR_ASPECT_VSYSTEM, actualReplica.getFQN().toString());
       // aspect.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+"name", "IP Config");
        Element aspect = doc.createElement(TCloudConstants.TAG_ASPECT);
        aspect.setAttribute("required", "true");
        aspect.setAttribute(TCloudConstants.ATTR_ASPECT_VSYSTEM, actualReplica.getFQN().toString());
        aspect.setAttribute("name", "IP Config");
        aspectsSection.appendChild(aspect);

        for (NIC nicItr: actualReplica.getNICs()) {
        	System.out.println ("NICsss " + nicItr.getIPAddresses());
        	  if (nicItr != null && nicItr.getIPAddresses()!= null && nicItr.getIPAddresses().size()>0 
              		&& nicItr.getIPAddresses().get(0)!= null)
              {
        		  
             //  Element netConfigAssociation = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_NETWORK_ASSOCIATION);
               Element netConfigAssociation = doc.createElement(TCloudConstants.TAG_NETWORK_ASSOCIATION);
               netConfigSection.appendChild(netConfigAssociation);
              // netConfigSection.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.ATTR_NETWORK_ASSOCIATION_HREF, serverURL + URICreation.getURINet(nicItr.getNICConf().getNetwork().getFQN().toString()));
               netConfigSection.setAttribute(TCloudConstants.ATTR_NETWORK_ASSOCIATION_HREF, serverURL + URICreation.getURINet(nicItr.getNICConf().getNetwork().getFQN().toString()));
            
            //	Element property = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_ASPECT_PROPERTY);
            	Element property = doc.createElement(TCloudConstants.TAG_ASPECT_PROPERTY);
                aspect.appendChild(property);
               // property.setAttributeNS("http://nuba.morfeo-project.org/network","ns0:"+"type", "string");
                property.setAttribute("type", "string");


             //   Element key = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_ASPECT_KEY);
                Element key = doc.createElement(TCloudConstants.TAG_ASPECT_KEY);
             //   key.appendChild(doc.createTextNode(nicItr.getNICConf().getNetwork().getFQN().toString()));
                key.appendChild(doc.createTextNode(nicItr.getNICConf().getNetwork().getName()));
                property.appendChild(key);
                
            //	Element value = doc.createElementNS("http://nuba.morfeo-project.org/network","ns0:"+TCloudConstants.TAG_ASPECT_VALUE);
            	Element value = doc.createElement(TCloudConstants.TAG_ASPECT_VALUE);
            value.appendChild(doc.createTextNode(nicItr.getIPAddresses().get(0)));
            property.appendChild(value);
            }
        }

        OutputFormat format    = new OutputFormat (doc); 
        // as a String
        StringWriter stringOut = new StringWriter ();    
        XMLSerializer serial   = new XMLSerializer (stringOut, 
                                                    format);
        try {
			serial.serialize(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Display the XML
        System.out.println("XML " + stringOut.toString());
        logger.info("XML " + stringOut.toString());
        return doc;
    }

    public Map<VEEReplica, VEEReplicaAllocationResult> allocateVEEReplicas(
            Set<VEEReplica> veeReplicasConfs,
            SubmissionModelType submissionModel)
            throws CommunicationErrorException, NotEnoughResourcesException,
            AccessDeniedException, VEEReplicaDescriptionMalformedException {

        if(veeReplicasConfs == null)
            throw new IllegalArgumentException("Set of VEE replicas configurations to allocate cannot be null.");

        for(VEEReplica veeReplica : veeReplicasConfs)
            if(veeReplica == null)
                throw new IllegalArgumentException("null VEEReplica found on deployment et.");

        if(submissionModel == null)
            throw new IllegalArgumentException("Submission model cannot be null");

        Map<VEEReplica, VEEReplicaAllocationResult> result = new HashMap<VEEReplica, VEEReplicaAllocationResult>();

        Map<VEEReplica, String> urlTasks = new HashMap<VEEReplica, String> ();

        for (VEEReplica actualReplica: veeReplicasConfs) {
        	
            
            

            // create the ficticious service
            
            if (SMConfiguration.getInstance().getNullVAppVm())
            {
            	logger.info("Gettting fictious");
            	
            	if (!checkServiceDeployed(actualReplica))
            	{
            	  boolean result2 = getFicticiousService (actualReplica);
            	  logger.info("Get fictions service " +result2);
            	  if (!result2)
            	  {
            		
            		throw new CommunicationErrorException("Error to create the null service");
            	  }
            	}
            	else
            	{
            		logger.info("The service is already created ");
            	}
            }
            
            Reference urlReplica = null;
            // Compose the URL for the replica.
        	if (SMConfiguration.getInstance().getNullVAppVm())
        	{
        		urlReplica = new Reference(serverURL + URICreation.getURIService(actualReplica.getFQN().toString())  + "/action/instantiateOvf");
        	}
        	else
        	{
                urlReplica = new Reference(serverURL + URICreation.getURIVapp(actualReplica.getFQN().toString())  + "/action/instantiateOvf");
        	}
            logger.info (urlReplica.toString());
            
            // Create de DOM Representation for the replica
            DomRepresentation data;
            try {
                data = new DomRepresentation(MediaType.APPLICATION_XML, getTCloudVMParameters(actualReplica));

            } catch (ParserConfigurationException e) {
                throw new VEEReplicaDescriptionMalformedException(e.getMessage(), actualReplica);
            }
           

            // Call the server with the URI and the data
            Response response = client.post(urlReplica, data);

            // Depending on the response code, return with an error, or wait for a response
            System.out.println ("Respuseta " + response.getStatus().getCode());
            switch (response.getStatus().getCode()) {

                case 401:    // Unauthorized
                case 403:    // Forbidden
                    // Throw an Access Denied Exception
                    throw new AccessDeniedException(response.getStatus().getDescription(), actualReplica, null);

                case 400:    // Bad Request
                case 404:    // Not found
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

                case 501:
                case 500:
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


                case 202:
                    // The VEE has been accepted to be deployed, but the response will be asynchronous.
                    // Wait for the response actively.

                case 201:

                case 200:
                    // The VirtualMachine has been started without errors. Parse the response and
                    // get the task id.

                    try {
                        Document responseXml = response.getEntityAsDom().getDocument();
                        logger.info(" tewst" + PaasUtils.tooString(responseXml));

                        NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);
                        
                        if (tasks.getLength()==0)
                        {
                        	tasks = responseXml.getElementsByTagName("ns24:Task");
                        }

                        if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("href")!= null) {
                            urlTasks.put(actualReplica, ((Element)tasks.item(0)).getAttribute("href"));
                        } else {
                            throw new CommunicationErrorException("Error retrieving task URL.", new Exception(response.getStatus().getName()));
                        }

                    } catch (IOException e) {
                        throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                    } catch (Throwable e) {
                    	e.printStackTrace();
                        throw new CommunicationErrorException("Internal error while decoding the answer", e);
                    }

                    break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }

        // Wait for the replicas to be returned.
        while (veeReplicasConfs.size() != result.size()) {
            for (VEEReplica actualReplica: veeReplicasConfs) {
                VEEReplicaAllocationResult deploymentResult = null;

                if (result.containsKey(actualReplica)) continue;
                
                Document xml = getTask (urlTasks.get(actualReplica));

                String status = getTaskStatus(xml);
                System.out.println ("Status" + status);

                if (status.toLowerCase().equals("success")) {
                    deploymentResult = new VEEReplicaAllocationResult(actualReplica.getId(), "Deployed", true);
                    result.put(actualReplica, deploymentResult);
                } else if (status.toLowerCase().equals("error")) {
                	String error = this.getTaskMessageError(xml);
                    deploymentResult = new VEEReplicaAllocationResult(actualReplica.getId(), "Failed:"+error, false);
                    System.out.println ("Error in the operation " +deploymentResult);
                    result.put(actualReplica, deploymentResult);
                }
            }

            try {
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {}
        }
        return result;
    }
    
    private boolean checkServiceDeployed (VEEReplica actualReplica) 
    {
    	
    	Reference urlReplica = new Reference(serverURL + URICreation.getURIVDC(actualReplica.getFQN().toString())  + "/vapp/"+actualReplica.getVEE().getServiceApplication().getSerAppName());
    	logger.info(urlReplica.toString());
    	// Create de DOM Representation for the replica
               // Call the server with the URI and the data
    	Response response = null;
    	try
    	{
    		response = client.get(urlReplica);
    	}
    	catch (Exception e)
    	{
    		logger.info("Error al obtener si existe el servicio " + e.getMessage());
    		return false;
    	}
       

        // Depending on the response code, return with an error, or wait for a response
        System.out.println ("Respuseta " + response.getStatus().getCode());
        switch (response.getStatus().getCode()) {

            case 401:    // Unauthorized
            case 403:    // Forbidden
                // Throw an Access Denied Exception
             

            case 400:    // Bad Request
            case 404:    // Not found
             

            case 501:
            case 500:
                return false;


            case 202:
                // The VEE has been accepted to be deployed, but the response will be asynchronous.
                // Wait for the response actively.

            case 201:

            case 200:
                // The VirtualMachine has been started without errors. Parse the response and
                // get the task id.

               return true;
        
               
        }
        return false;
    }
    
    private boolean getFicticiousService (VEEReplica actualReplica) throws AccessDeniedException, CommunicationErrorException
    {
    	
    	Reference urlReplica = new Reference(serverURL + URICreation.getURIVDC(actualReplica.getFQN().toString())  + "/action/instantiateOvf");
    	logger.info(urlReplica.toString());
    	// Create de DOM Representation for the replica
        DomRepresentation data;
        data = new DomRepresentation(MediaType.APPLICATION_XML, getFictiousService(actualReplica));
       

        // Call the server with the URI and the data
        Response response = client.post(urlReplica, data);

        // Depending on the response code, return with an error, or wait for a response
        System.out.println ("Respuseta " + response.getStatus().getCode());
        switch (response.getStatus().getCode()) {

            case 401:    // Unauthorized
            case 403:    // Forbidden
                // Throw an Access Denied Exception
                throw new AccessDeniedException(response.getStatus().getDescription(), actualReplica, null);

            case 400:    // Bad Request
            case 404:    // Not found
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

            case 501:
            case 500:
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


            case 202:
                // The VEE has been accepted to be deployed, but the response will be asynchronous.
                // Wait for the response actively.

            case 201:

            case 200:
                // The VirtualMachine has been started without errors. Parse the response and
                // get the task id.

               return true;
        
               
        }
        return false;
    }

    private String   getTaskStatus(Document responseXml) throws AccessDeniedException, CommunicationErrorException {
    	
    	
    	if (responseXml == null)
    		return "unknown";
    	 NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);
    	 
    	 if (tasks.getLength()==0)
    	 {
    		 tasks = responseXml.getElementsByTagName("ns24:Task");
    	 }

         if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("status")!= null) {

             // If the task is Success or Error, store the result.
             return ((Element)tasks.item(0)).getAttribute("status");

         } 
         return "unknown";
    }
    
    private String   getTaskStatus(String url) throws AccessDeniedException, CommunicationErrorException {
    	
    	Document responseXml = getTask (url);
    	if (responseXml == null)
    		return "unknown";
    	 NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);

    	 if (tasks.getLength()==0)
    	 {
    		 tasks = responseXml.getElementsByTagName("ns24:Task");
    	 }
         if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("status")!= null) {

             // If the task is Success or Error, store the result.
             return ((Element)tasks.item(0)).getAttribute("status");

         } 
         return "unknown";
    }
    
    public static void main (String [] args)
    {
    	TCloudClient d = new TCloudClient ("http://");
    	try {
			System.out.println (d.getTaskStatus("http://84.21.173.137:8080/fco/api/org/CESGA/vdc/dddd/task/1"));
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    private Document getTask(String url) throws AccessDeniedException, CommunicationErrorException {
        // Check the state of the task
        Reference urlTask = new Reference(url);

        System.out.println ("URL " + url);
        Response response = client.get(urlTask);

        switch (response.getStatus().getCode()) {

            case 401:    // Unauthorized
            case 403:    // Forbidden
                throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

            case 400:    // Bad Request
            case 404:    // Not found
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

            case 501:
            case 500:
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


            case 202:
                // The VEE has been accepted to be deployed, but the response will be asynchronous.
                // Wait for the response actively.

            case 201:

            case 200:
                // The VirtualMachine has been started without errors. Parse the response and
                // get the task id.
                try {
                    Document responseXml = response.getEntityAsDom().getDocument();
                    return responseXml;
                   

                } catch (IOException e) {
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                } catch (Throwable e) {
                    throw new CommunicationErrorException("Internal error while decoding the answer", e);
                }
        }

        return null;
    }
    
    private String getTaskMessageError(Document message) throws AccessDeniedException, CommunicationErrorException {


        NodeList tasks = message.getElementsByTagName("error");
        
        if (tasks.getLength()==0)
        {
        	tasks = message.getElementsByTagName("ns24:Error") ;
        }

        if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("message")!= null) {

                    	
           return ((Element)tasks.item(0)).getAttribute("message");
       
        }

        return "";
    }

    public void deleteNetwork(Set<Network> conf)
            throws AccessDeniedException, CommunicationErrorException {

        if(conf == null)
            throw new IllegalArgumentException("Set of networks configurations to allocate cannot be null");

        for (Network networkConf: conf) {
        	
        	if (networkConf.getName().equals("public"))
        	 continue;

            Reference urlNetwork = new Reference(serverURL + URICreation.getURINet(networkConf.getFQN().toString()));

            // Call the server with the URI and the data
            Response response = client.delete(urlNetwork);

            // Depending on the response code, return with an error, or wait for a response
            switch (response.getStatus().getCode()) {

                case 401:    // Unauthorized
                case 403:    // Forbidden
                    // Throw an Access Denied Exception
                    logger.error("Not enough privileges to access the network information.");
                    throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

                case 400:    // Bad Request
                case 404:    // Not found
                    logger.error("The resource was not found on the server; the OCCI server may be misconfigured, or the URL may be wrong.");
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

                case 501:
                case 500:
                    logger.error("Internal error in the VEEM OCCI server: " + response.getStatus().getDescription());
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

                case 202:
                case 201:
                case 200:
                    logger.info("Network successfully deleted.");
                    break;
            }
        }

    }

    public Network getNetwork(Network conf)
            throws AccessDeniedException, CommunicationErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    public VEEReplica getVEEReplicaHwState(VEEReplica veeReplica)
            throws CommunicationErrorException {
        // TODO Auto-generated method stub
        return null;
    }
    

    public Set<VEEReplica> getVEEReplicas() throws CommunicationErrorException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getVEEReplicaXML (VEEReplica veeReplica)
    throws CommunicationErrorException, AccessDeniedException {
    	
     /*  GetVEE vee = new GetVEE ();
		String fqn = veeReplica.getFQN().toString();
		String ip = vee.getIp(fqn);
		Document doc = vee.obtainXMLVEE (fqn,ip);
		String text = vee.tooString(doc);
		if (true)
		return text;*/
		
    	Reference urlReplica = new Reference(serverURL + URICreation.getURIVEEReplica(veeReplica.getFQN().toString()));
    	
    	logger.info (" Url network " + urlReplica.toString());

        // Call the server with the URI and the data
        Response response = client.get(urlReplica);

        // Depending on the response code, return with an error, or wait for a response
        switch (response.getStatus().getCode()) {

            case 401:    // Unauthorized
            case 403:    // Forbidden
                // Throw an Access Denied Exception
                logger.error("Not enough privileges to access the VM information.");
                throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

            case 400:    // Bad Request
            case 404:    // Not found
                logger.error("The resource was not found on the server; the tcloud server may be misconfigured, or the URL may be wrong.");
                
                try
                {
                  return getVEEReplicaXMLForNUBA (veeReplica);
                }
                catch (Exception e)
                {
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                }
            case 501:
            case 500:
                logger.error("Internal error in the VEEM tcloud server: " + response.getStatus().getDescription());
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

            case 202:
            case 201:
            case 200:
                logger.info("Operation suceesfully done.");
                
                try {
                    Document responseXml = response.getEntityAsDom().getDocument();
                    return com.telefonica.claudia.slm.paas.PaasUtils.tooString(responseXml);
                } 
                catch (Throwable e) 
                {
                       throw new CommunicationErrorException("Internal error while decoding the answer", e);
                }
                    
                
        }
        return null;
   }
    
    public String getVEEReplicaXMLForNUBA (VEEReplica veeReplica)
    throws CommunicationErrorException, AccessDeniedException {
    	
     /*  GetVEE vee = new GetVEE ();
		String fqn = veeReplica.getFQN().toString();
		String ip = vee.getIp(fqn);
		Document doc = vee.obtainXMLVEE (fqn,ip);
		String text = vee.tooString(doc);
		if (true)
		return text;*/
		
    	Reference urlReplica = new Reference(serverURL + URICreation.getURIVEE(veeReplica.getVEE().getFQN().toString()));
    	logger.info (serverURL + URICreation.getURIVEE(veeReplica.getVEE().getFQN().toString()));

        // Call the server with the URI and the data
        Response response = client.get(urlReplica);

        // Depending on the response code, return with an error, or wait for a response
        switch (response.getStatus().getCode()) {

            case 401:    // Unauthorized
            case 403:    // Forbidden
                // Throw an Access Denied Exception
                logger.error("Not enough privileges to access the VM information.");
                throw new AccessDeniedException(response.getStatus().getDescription(), null, null);

            case 400:    // Bad Request
            case 404:    // Not found
                logger.error("The resource was not found on the server; the tcloud server may be misconfigured, or the URL may be wrong.");
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

            case 501:
            case 500:
                logger.error("Internal error in the VEEM tcloud server: " + response.getStatus().getDescription());
                throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

            case 202:
            case 201:
            case 200:
                logger.info("Operation suceesfully done.");
                
                try {
                    Document responseXml = response.getEntityAsDom().getDocument();
                    return com.telefonica.claudia.slm.paas.PaasUtils.tooString(responseXml);
                } 
                catch (Throwable e) 
                {
                       throw new CommunicationErrorException("Internal error while decoding the answer", e);
                }
                    
                
        }
        return null;
   }

    public void sendACD(ServiceApplication sa) {
        // TODO Auto-generated method stub

    }

    public Map<VEEReplica, VEEReplicaUpdateResult> shutdownReplica(Set<VEEReplica> veeReplicas) throws CommunicationErrorException, AccessDeniedException {

        if(veeReplicas == null)
            throw new IllegalArgumentException("Set of VEE replicas to update cannot be null");
        for(VEEReplica veeReplica : veeReplicas)
            if(veeReplica == null)
                throw new IllegalArgumentException("Null VEE in set of VEEs to update");

        Map<VEEReplica, VEEReplicaUpdateResult> result = new HashMap<VEEReplica, VEEReplicaUpdateResult>();

        Map<VEEReplica, String> urlTasks = new HashMap<VEEReplica, String> ();

        for (VEEReplica actualReplica: veeReplicas) {

            // Compose the URL for the replica.
            Reference urlReplica = new Reference(serverURL + URICreation.getURIVEEReplica(actualReplica.getFQN().toString()));
            logger.info("PONG urlReplica= "+ serverURL + URICreation.getURIVEEReplica(actualReplica.getFQN().toString()));

            // Call the server with the URI and the data
            Response response = client.delete(urlReplica);
            logger.info("PONG response.getStatus().getCode() = "+ response.getStatus().getCode());

            // Depending on the response code, return with an error, or wait for a response
            switch (response.getStatus().getCode()) {

                case 401:    // Unauthorized
                case 403:    // Forbidden
                    // Throw an Access Denied Exception
                    throw new AccessDeniedException(response.getStatus().getDescription(), actualReplica, null);

                case 400:    // Bad Request
                case 404:    // Not found
                   // throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                	shutdownReplicaNuba (actualReplica);

                case 501:
                case 500:
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


                case 202:
                    // The VEE has been accepted to be deployed, but the response will be asynchronous.
                    // Wait for the response actively.

                case 201:

                case 200:
                    // The VirtualMachine has been started without errors. Parse the response and
                    // get the task id.

                    try {
                        Document responseXml = response.getEntityAsDom().getDocument();
                        
                        System.out.println (responseXml);

                        NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);

                        if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("href")!= null) {
                            urlTasks.put(actualReplica, ((Element)tasks.item(0)).getAttribute("href"));
                        } else {
                            throw new CommunicationErrorException("Error retrieving task URL.", new Exception(response.getStatus().getName()));
                        }

                    } catch (IOException e) {
                        throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                    } catch (Throwable e) {
                        throw new CommunicationErrorException("Internal error while decoding the answer", e);
                    }

                    break;
            }

            try {Thread.sleep(4000);} catch (Throwable t) {}
        }

        while (veeReplicas.size() != result.size()) {
            for (VEEReplica actualReplica: veeReplicas) {
                VEEReplicaUpdateResult deploymentResult = null;

                if (result.containsKey(actualReplica)) continue;

                String status = getTaskStatus(urlTasks.get(actualReplica));

                if (status.toLowerCase().equals("success")) {
                    deploymentResult = new VEEReplicaUpdateResult(actualReplica.getId(), "Deployed", true);
                    result.put(actualReplica, deploymentResult);
                } else if (status.toLowerCase().equals("error")) {
                	
                    deploymentResult = new VEEReplicaUpdateResult(actualReplica.getId(), "Failed", false);
                    System.out.println ("Error in the operation " +deploymentResult);
                    result.put(actualReplica, deploymentResult);
                }
            }

            try {
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {}
        }

        return result;
    }
    
    public Map<VEEReplica, VEEReplicaUpdateResult> shutdownReplicaNuba(VEEReplica actualReplica) throws CommunicationErrorException, AccessDeniedException {

      
        
        Map<VEEReplica, VEEReplicaUpdateResult> result = new HashMap<VEEReplica, VEEReplicaUpdateResult>();

        Map<VEEReplica, String> urlTasks = new HashMap<VEEReplica, String> ();

      

            // Compose the URL for the replica.
            Reference urlReplica = new Reference(serverURL + URICreation.getURIVEE(actualReplica.getFQN().toString()));
            logger.info("PONG urlReplica= "+ serverURL + URICreation.getURIVEEReplica(actualReplica.getFQN().toString()));

            // Call the server with the URI and the data
            Response response = client.delete(urlReplica);
            logger.info("PONG response.getStatus().getCode() = "+ response.getStatus().getCode());

            // Depending on the response code, return with an error, or wait for a response
            switch (response.getStatus().getCode()) {

                case 401:    // Unauthorized
                case 403:    // Forbidden
                    // Throw an Access Denied Exception
                    throw new AccessDeniedException(response.getStatus().getDescription(), actualReplica, null);

                case 400:    // Bad Request
                case 404:    // Not found
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));

                case 501:
                case 500:
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));


                case 202:
                    // The VEE has been accepted to be deployed, but the response will be asynchronous.
                    // Wait for the response actively.

                case 201:

                case 200:
                    // The VirtualMachine has been started without errors. Parse the response and
                    // get the task id.

                    try {
                        Document responseXml = response.getEntityAsDom().getDocument();
                        
                        System.out.println (responseXml);

                        NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);

                        if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("href")!= null) {
                            urlTasks.put(actualReplica, ((Element)tasks.item(0)).getAttribute("href"));
                        } else {
                            throw new CommunicationErrorException("Error retrieving task URL.", new Exception(response.getStatus().getName()));
                        }

                    } catch (IOException e) {
                        throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                    } catch (Throwable e) {
                        throw new CommunicationErrorException("Internal error while decoding the answer", e);
                    }

                    break;
            }


        return result;
    }
    
    public DOMImplementation getDomImplementation() throws Exception {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory
        .newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation domImpl = db.getDOMImplementation();
        return domImpl;
        
    }

    private Document buildNetworksWithNamespaces(
            DOMImplementation domImpl) {
        Document document = domImpl.createDocument("http://nuba.morfeo-project.org/network",
                "ns1:Network", null);
       
        document.getDocumentElement().setAttributeNS("http://nuba.morfeo-project.org/network","ns1:name", "value");
        Element element = document.createElementNS("http://nuba.morfeo-project.org/network",
                "ns1:Base");
        element.appendChild(document.createTextNode("10.22.22.22"));
        
        Element element2 = document.createElementNS("http://nuba.morfeo-project.org/network",
        "ns1:Mask");
        element2.appendChild(document.createTextNode("255.555.555.555."));
        document.getDocumentElement().appendChild(element);
        document.getDocumentElement().appendChild(element2);
        return document;
    }

    private void serialize(DOMImplementation domImpl, Document document) {
        DOMImplementationLS ls = (DOMImplementationLS) domImpl;
        LSSerializer lss = ls.createLSSerializer();
        LSOutput lso = ls.createLSOutput();
        lso.setByteStream(System.out);
        lss.write(document, lso);
    }

}
