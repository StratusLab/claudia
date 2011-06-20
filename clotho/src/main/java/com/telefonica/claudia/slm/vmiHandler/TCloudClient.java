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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.NotEnoughResourcesException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.VEEReplicaDescriptionMalformedException;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

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


    private Document getTCloudNetworkParameters(Network networkConf) throws ParserConfigurationException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();

        
        String[] networkAddresses2 = SMConfiguration.getInstance().getNetworkRanges();
        String range2 = networkAddresses2[0];
        
        int DNSPos= range2.indexOf("DNS:");
        String dns = null;
        if (DNSPos>0) {
            dns = range2.substring(DNSPos+4, range2.indexOf(";", DNSPos)).trim();
        }
        logger.info("PONG DNS section: " + dns);
        
        int GWPos= range2.indexOf("Gateway:");
        String gw = null;
        if (GWPos>0) {
            gw = range2.substring(GWPos+8, range2.indexOf(";", GWPos)).trim();
        }
        logger.info("PONG Gateway section: " + gw);
        
        Element root = doc.createElement(TCloudConstants.TAG_NETWORK_ROOT);
        root.setAttribute(TCloudConstants.ATTR_NETWORK_NAME, networkConf.getFQN().toString());
        root.setAttribute(TCloudConstants.TAG_NETWORK_DNS, dns);
        root.setAttribute(TCloudConstants.TAG_NETWORK_GATEWAY,gw);
        doc.appendChild(root);

        Element baseAddress = doc.createElement(TCloudConstants.TAG_NETWORK_BASE_ADDRESS);

        baseAddress.appendChild(doc.createTextNode(networkConf.getNetworkAddresses()[0]));
        root.appendChild(baseAddress);

        Element netmask = doc.createElement(TCloudConstants.TAG_NETWORK_NETMASK);
        netmask.appendChild(doc.createTextNode(networkConf.getNetworkAddresses()[1]));
        root.appendChild(netmask);
        
        

//        Element dns = doc.createElement(TCloudConstants.TAG_NETWORK_DNS);
//        dns.appendChild(doc.createTextNode(dnss));
//        root.appendChild(dns);
//      //  logger.info("PONG DNS section: " + networkConf.getNetworkAddresses()[2]);
//        logger.info("PONG DNS section: " + dnss);
//        
//
//        Element gateway = doc.createElement(TCloudConstants.TAG_NETWORK_GATEWAY);
//        gateway.appendChild(doc.createTextNode(gw));
//        root.appendChild(gateway);
//      //  logger.info("PONG DNS section: " + networkConf.getNetworkAddresses()[3]);
//        logger.info("PONG Gateway section: " + gw);



         String macEnable = SMConfiguration.getInstance().getNetworkMacEnable();

        if (macEnable.equals("true")){


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

    public void allocateNetwork(Set<Network> conf)
            throws AccessDeniedException, CommunicationErrorException, NotEnoughResourcesException {
        if(conf == null)
            throw new IllegalArgumentException("Set of netowrks configurations to allocate cannot be null");

        Map<Network, String> urlTasks = new HashMap<Network, String> ();

        for (Network networkConf: conf) {

            // Compose the URL for the network.
            Reference urlNetwork = new Reference(serverURL + URICreation.getURINetAdd(networkConf.getFQN().toString()));

            // Create de DOM Representation for the network configuration
            DomRepresentation data;
            try {
                data = new DomRepresentation(MediaType.APPLICATION_XML, getTCloudNetworkParameters(networkConf));

            } catch (ParserConfigurationException e) {
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

                        NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);

                        if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("href")!= null) {
                            urlTasks.put(networkConf, ((Element)tasks.item(0)).getAttribute("href"));
                        } else {
                            throw new CommunicationErrorException("Error retrieving task URL.", new Exception(response.getStatus().getName()));
                        }

                        logger.info("Network " + networkConf + "successfully allocated.");
                    } catch (IOException e) {
                        logger.error("Error parsing the response from the server: " + e.getMessage());
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

    private Document getTCloudVMParameters(VEEReplica actualReplica) throws ParserConfigurationException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement(TCloudConstants.TAG_INSTANTIATE_OVF);
        doc.appendChild(root);
        root.setAttribute(TCloudConstants.ATTR_INSTANTIATION_PARAMS_NAME, String.valueOf(actualReplica.getFQN().toString()));

        // Add the ovf envelope to the root.
        if (actualReplica.getOvfRepresentation()!=null && !actualReplica.getOvfRepresentation().equals("")) {
            try {
                Document ovfEnvelope = builder.parse(new ByteArrayInputStream(actualReplica.getOvfRepresentation().getBytes()));

                root.appendChild(doc.importNode(ovfEnvelope.getFirstChild(), true));

            } catch (SAXException e) {
                logger.error("Error parsing the OVF for replica generation: " + e.getMessage());
            } catch (IOException e) {
                logger.error("Error retrieving OVF from the data model: " + e.getMessage());
            }
        }

        Element instantiationParams  = doc.createElement(TCloudConstants.TAG_INSTANTIATION_PARAMS);
        root.appendChild(instantiationParams);

        Element netConfigSection = doc.createElement(TCloudConstants.TAG_NETWORK_CONFIG_SECTION);
        instantiationParams.appendChild(netConfigSection);

        Element netConfig = doc.createElement(TCloudConstants.TAG_NETWORK_CONFIG);
        netConfigSection.appendChild(netConfig);

        Element aspectsSection = doc.createElement(TCloudConstants.TAG_ASPECTS_SECTION);
        instantiationParams.appendChild(aspectsSection);

        Element aspect = doc.createElement(TCloudConstants.TAG_ASPECT);
        aspect.setAttribute("required", "true");
        aspect.setAttribute(TCloudConstants.ATTR_ASPECT_VSYSTEM, actualReplica.getFQN().toString());
        aspect.setAttribute("name", "IP Config");
        aspectsSection.appendChild(aspect);

        for (NIC nicItr: actualReplica.getNICs()) {
            Element netConfigAssociation = doc.createElement(TCloudConstants.TAG_NETWORK_ASSOCIATION);
            netConfigSection.appendChild(netConfigAssociation);
            netConfigSection.setAttribute(TCloudConstants.ATTR_NETWORK_ASSOCIATION_HREF, serverURL + URICreation.getURINet(nicItr.getNICConf().getNetwork().getFQN().toString()));

            Element property = doc.createElement(TCloudConstants.TAG_ASPECT_PROPERTY);
            aspect.appendChild(property);
            property.setAttribute("type", "string");

            Element key = doc.createElement(TCloudConstants.TAG_ASPECT_KEY);
            key.appendChild(doc.createTextNode(nicItr.getNICConf().getNetwork().getFQN().toString()));
            property.appendChild(key);

            Element value = doc.createElement(TCloudConstants.TAG_ASPECT_VALUE);
            value.appendChild(doc.createTextNode(nicItr.getIPAddresses().get(0)));
            property.appendChild(value);
        }

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

            // Compose the URL for the replica.
            Reference urlReplica = new Reference(serverURL + URICreation.getURIVapp(actualReplica.getFQN().toString())  + "/action/instantiateOvf");

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

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }

        // Wait for the replicas to be returned.
        while (veeReplicasConfs.size() != result.size()) {
            for (VEEReplica actualReplica: veeReplicasConfs) {
                VEEReplicaAllocationResult deploymentResult = null;

                if (result.containsKey(actualReplica)) continue;

                String status = getTaskStatus(urlTasks.get(actualReplica));

                if (status.toLowerCase().equals("success")) {
                    deploymentResult = new VEEReplicaAllocationResult(actualReplica.getId(), "Deployed", true);
                    result.put(actualReplica, deploymentResult);
                } else if (status.toLowerCase().equals("error")) {
                    deploymentResult = new VEEReplicaAllocationResult(actualReplica.getId(), "Failed", false);
                    result.put(actualReplica, deploymentResult);
                }
            }

            try {
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {}
        }
        return result;
    }


    private String getTaskStatus(String url) throws AccessDeniedException, CommunicationErrorException {
        // Check the state of the task
        Reference urlTask = new Reference(url);

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

                    NodeList tasks = responseXml.getElementsByTagName(TCloudConstants.TAG_TASK);

                    if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("status")!= null) {

                        // If the task is Success or Error, store the result.
                        return ((Element)tasks.item(0)).getAttribute("status");

                    } else {
                        throw new CommunicationErrorException("Error retrieving task URL.", new Exception(response.getStatus().getName()));
                    }

                } catch (IOException e) {
                    throw new CommunicationErrorException(response.getStatus().getDescription(), new Exception(response.getStatus().getName()));
                } catch (Throwable e) {
                    throw new CommunicationErrorException("Internal error while decoding the answer", e);
                }
        }

        return "unknown";
    }

    public void deleteNetwork(Set<Network> conf)
            throws AccessDeniedException, CommunicationErrorException {

        if(conf == null)
            throw new IllegalArgumentException("Set of networks configurations to allocate cannot be null");

        for (Network networkConf: conf) {

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
                    result.put(actualReplica, deploymentResult);
                }
            }

            try {
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {}
        }

        return result;
    }
}
