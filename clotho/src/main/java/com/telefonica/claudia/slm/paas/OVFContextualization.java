package com.telefonica.claudia.slm.paas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.DiskSectionType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.FileType;
import org.dmtf.schemas.ovf.envelope._1.ReferencesType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemCollectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;

import com.abiquo.ovf.OVFEnvelopeUtils;
import com.abiquo.ovf.OVFEnvironmentUtils;
import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.EmptyEnvelopeException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.InvalidSectionException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;
import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.NICConf;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;
import com.telefonica.claudia.slm.lifecyclemanager.FSM;
import com.telefonica.claudia.smi.URICreation;

public class OVFContextualization {
	
	//
	private static final String repositoryDir = "./repository";
	private static final String customImagesDir = repositoryDir
	+ SMConfiguration.getInstance().getImagesServerPath();
	private static final String protocol = "http://";

	private static Logger logger = Logger.getLogger("OVFContextualization");

	public String getMacro (VEE vee, String macro)
	{
		if (macro.equals("@ServiceId"))
		{
		  return getServiceId (vee);
		}
		else if (macro.equals("@MonitoringChannel"))
		{
			return getMonitoringChannel ();
		}
		else if (macro.equals("@Username"))
		{
			return vee.getUserName();
		}
		else if (macro.equals("@Password"))
		{
			return vee.getPassword();
		}
		else if (macro.equals("@IdServiceMonitoring"))
		{
			return getMd5FromFQN (vee);
		}
		else if (macro.startsWith("@Property"))
		{
			String product = macro.substring(macro.indexOf("(")+1,macro.indexOf(","));
			String property = macro.substring(macro.indexOf(",")+1,macro.indexOf(")"));
			return getPropertyFromOtherProduct (vee,product,property);
		}
		
		else if (macro.startsWith("@IP"))
		{
			if (macro.indexOf(",")!=-1)
			{
				return getIpVmName (vee, macro.substring(macro.indexOf("(")+1,macro.indexOf(","))
						, macro.substring(macro.indexOf(",")+1,macro.indexOf(")")));
			}
			else
			{
				return getIpVm (vee,macro.substring(macro.indexOf("(")+1,macro.indexOf(")")));
			}
		}
		return null;
		
	}
	
	public String getPropertyFromOtherProduct (VEE vee, String productname, String property)
	{
	   Product product = null;
	   if ((product= vee.getProductByName (productname)) != null)
	   {
		   Property prop = null;
		   if ((prop = product.getPropertyByNameFinished(property))!= null)
		   {
			   return prop.getValue();
		   }
	   }
		return null;
	}
	
	public String getMd5FromFQN (VEE vee) 
	{
		String fqn = vee.getServiceApplication().getFQN().toString();
		
		 
        MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return null;
		}
        md.update(fqn.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
      
		return sb.toString();
	}
	
	public String getServiceId (VEE vee)
	{
		//@ServiceId
		return vee.getServiceApplication().getFQN().toString();
	}
	
	public String getMonitoringChannel ()
	{
		//@MonitoringChannel.
		return SMConfiguration.getInstance().getMonitoringAddress();
	}
	
	//@id([n]), 
	
	
	//=“@IP(net1)” 
	public String getIpVm (VEE vee, String namenetwork)
	{
		// We chose the first one
		for (VEEReplica replica: vee.getVEEReplicas())
		{
			for (NIC nic: replica.getNICs())
			{
				Network net = nic.getNICConf().getNetwork();
				if (net.getName().equals(namenetwork))
				{
					return net.getNetworkAddresses()[0];
				}
			}
		}
		return null;
	}
	
	//@IP(sge_net,VIP)
	public String getIpVmName (VEE vee, String namenetwork, String namevee)
	{
		// We chose the first one
		VEE veerelated = null;
		for (VEE vee2: vee.getServiceApplication().getVEEs())
		{
			if (vee2.getVEEName().equals(namevee))
				veerelated = vee2;
		}
		
		return getIpVm (veerelated, namenetwork);
	
	}
	
	public String getOvfVEEwithContextualization (String ovf, String url) throws UnsupportedEncodingException, XMLException
	{
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		
		EnvelopeType envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(ovf.getBytes("UTF-8")));
		FileType file = new FileType ();
		file.setHref(url);
		file.setId("contextualization");
		envelope.getReferences().getFile().add(file);
		return ovfSerializer.writeXML(envelope);
	}
	public String generateOvfEnvironment (VEEReplica replica)
	{
	
		HashMap<String,ArrayList<String>> map = getIpsForMacros (replica);
		HashMap<String, HashMap<String, String> > entry = getEntryPoints(replica);
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		String xmlvs = replica.getVEE().getOvfRepresentation();
		System.out.println (xmlvs);
		VirtualSystemType vs = null;
		try {
			EnvelopeType envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(xmlvs.getBytes("UTF-8")));
			vs = (VirtualSystemType)OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envelope);
		//	vs = OVFEnvelopeUtils.getVirtualSystems ((VirtualSystemCollectionType)entityInstance);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (XMLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (EmptyEnvelopeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	try {
		OVFEnvironmentUtils.createOVFEnvironment( vs,
				replica.getId(),
				SMConfiguration.getInstance().getDomainName(),
				URICreation.getFQN(SMConfiguration.getInstance().getSiteRoot(), replica.getVEE().getServiceApplication().getCustomer().getCustomerName(),
						 replica.getVEE().getServiceApplication().getSerAppName()),
						 URICreation.getFQN(SMConfiguration.getInstance().getSiteRoot(), replica.getVEE().getServiceApplication().getCustomer().getCustomerName(),
								 replica.getVEE().getServiceApplication().getSerAppName(), replica.getVEE().getVEEName()),
						 
						 SMConfiguration.getInstance().getMonitoringAddress(), 
				map,
				null,
				null,
				null,
				entry,
				output,
				true);
	} catch (IPNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DNSServerNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NetmaskNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (GatewayNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PrecedentTierEntryPointNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NotEnoughIPsInPoolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PoolNameNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println("OVF Environment file for VM [" +  output.toString()+ "] ");
	return output.toString();
	}
	
	public HashMap<String, String> getIpsNetwork (ServiceApplication sap, String network)
	{
		HashMap<String, String> aNetwork = new HashMap<String, String> ();
		
		for (VEE vee: sap.getVEEs())
			
		{
			for (NICConf nicconf : vee.getNICsConf())
			{
				if (!nicconf.getNetwork().getName().equals(network))
				{
					continue;
				}
				else
				{
					for (VEEReplica replica: vee.getVEEReplicas())
					{
						String ip = getIpNetworkVEEReplica (network,replica);
						
						if (ip!=null)
						aNetwork.put(vee.getVEEName(), ip);
					}
				}
			}
			
		}
		return aNetwork;
		
	}
	
	public List<String> getNetworksVEE (VEE vm)
	{
		List<String> networks = new ArrayList ();
		for (VEE vee: vm.getServiceApplication().getVEEs())
		{
		for (NICConf nicconf : vee.getNICsConf())
		{
			
			if (!networks.contains(nicconf.getNetwork().getName()))
			{
				networks.add(nicconf.getNetwork().getName());
				System.out.println("adding net " + nicconf.getNetwork().getName());
			}
			
		}
		}
		return networks;
	}
	public String getIpNetworkVEEReplica (String network, VEEReplica replica)
	{
		String ipstring = null;
		for (NIC nic: replica.getNICs())
		{
			NICConf conf = nic.getNICConf();
			String networkname = conf.getNetwork().getName();
			if (!networkname.equals(network))
			{
				continue;
			}
			
			
			if (nic.getIPAddresses()==null || nic.getIPAddresses().size()==0 ) 
				continue;
			ipstring = nic.getIPAddresses().get(0);
			
		
		}
		return ipstring;
	}
	
	
	public String createCustomizationFile(VEEReplica veeReplica) {

		String customizationDirName = customImagesDir + "/"
		+ veeReplica.getFQN().toString();
		File customizationDir = new File(customizationDirName);
		customizationDir.mkdirs();

		String customizationFileName = customizationDirName + "/ovf-env.xml";
		File customizationFile = new File(customizationFileName);
		logger.info("Creating customization file in "
				+ customizationFile.getPath());

		String customizationDirURLPath = SMConfiguration.getInstance()
		.getImagesServerPath()
		+ "/" + veeReplica.getFQN().toString();

		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(customizationFile));
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(FSM.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
			return ex.getMessage();
		}
		out.write(generateOvfEnvironment(veeReplica));
		out.close();

		String urlToCustomFile = protocol
		+ SMConfiguration.getInstance().getImagesServerHost() + ":"
		+ SMConfiguration.getInstance().getImagesServerPort()
		+ customizationDirURLPath + "/ovf-env.xml";

		logger.info("URL to customization file: " + urlToCustomFile);

		return urlToCustomFile;
	}
	
	public String updateOvfRepresentation (VEEReplica replica)
	{
		
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		String xmlvs = replica.getOvfRepresentation();
		org.dmtf.schemas.ovf.envelope._1.ReferencesType diskSection = null;
		EnvelopeType envelope = null;
		try {
			envelope = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(xmlvs.getBytes("UTF-8")));
			diskSection = envelope.getReferences();
		
		//	vs = OVFEnvelopeUtils.getVirtualSystems ((VirtualSystemCollectionType)entityInstance);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (XMLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		
		org.dmtf.schemas.ovf.envelope._1.FileType file = new FileType ();
		
		file.setHref(replica.getCustomizationFile());
		file.setId("ovfcontext");
		diskSection.getFile().add(file);
		
		
		
		try {
			return ovfSerializer.writeXML(envelope);
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
		
		
		
	}
	
	
	public String macroReplacement (VEEReplica replica) throws IOException 
	{
		HashMap<String,ArrayList<String>> map = getIpsForMacros (replica);
		HashMap<String, HashMap<String, String> > entry = getEntryPoints(replica);
		
		return inEnvolopeMacroReplacement(replica.getVEE().getOvfRepresentation(),    
				replica.getId(), 
				SMConfiguration.getInstance().getDomainName(), 
				URICreation.getFQN(SMConfiguration.getInstance().getSiteRoot(), replica.getVEE().getServiceApplication().getCustomer().getCustomerName(),
						 replica.getVEE().getServiceApplication().getSerAppName()), 
				URICreation.getFQN(SMConfiguration.getInstance().getSiteRoot(), replica.getVEE().getServiceApplication().getCustomer().getCustomerName(),
							 replica.getVEE().getServiceApplication().getSerAppName(), replica.getVEE().getVEEName()), 
						 SMConfiguration.getInstance().getMonitoringAddress(), 
						 map, 
				null, 
				null, 
				null,
				entry) ;
	}
	
	public String inEnvolopeMacroReplacement(String ovf,    
			int instanceNumber, 
			String domain, 
			String serviceId, 
			String veeId,
			String monitoringChannel, 
			HashMap<String,ArrayList<String>> ips, 
			HashMap<String, String> netmasks, 
			HashMap<String, String> dnsServers, 
			HashMap<String, String> gateways,
			HashMap<String, HashMap<String, String> > entryPoints) throws IOException {


		// Parse the ovf String
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		try {
			ovfSerializer.setValidateXML(false);

			EnvelopeType envVee = ovfSerializer.readXMLEnvelope(new ByteArrayInputStream(ovf.getBytes()));

			ContentType entityInstance = null;
			entityInstance = OVFEnvelopeUtils.getTopLevelVirtualSystemContent(envVee);

			if (entityInstance instanceof VirtualSystemCollectionType) {
				return "";
			} else if (entityInstance instanceof VirtualSystemType) {

				OVFEnvelopeUtils.inEnvolopeMacroReplacement(envVee, (VirtualSystemType) entityInstance, instanceNumber, domain, serviceId, veeId, monitoringChannel, ips,
						netmasks, dnsServers, gateways, entryPoints);	
				// Serialize the ovf
				ByteArrayOutputStream sob = new ByteArrayOutputStream();

				OVFSerializer.getInstance().writeXML(envVee, sob);

				return sob.toString(Charset.defaultCharset().toString());
			} else {
				throw new IllegalArgumentException("Virtual System not found.");
			}
		} catch (EmptyEnvelopeException e) {
			logger.error("Empty envelope found: " + e);
			throw new IOException("Empty envelope found: " + e.getMessage());
		}catch (JAXBException e) {
			logger.error("Unknown JAXB error");
			throw new IOException("Unexpected errors in macro replacement." + e.getMessage());
		} catch (XMLException e) {
			logger.error("OVF could not be serialized: " + e.getMessage());
			throw new IOException("Unexpected errors in macro replacement." + e.getMessage());
		} catch (IPNotFoundException e) {
			throw new IllegalArgumentException("No IP found: " + e.getMessage());
		} catch (DNSServerNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No DNS found: " + e.getMessage());
		} catch (NetmaskNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No Netmask found: " + e.getMessage());
		} catch (GatewayNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No Gateway found: " + e.getMessage());
		} catch (PrecedentTierEntryPointNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No Precedent Tier found: " + e.getMessage());
		} catch (NotEnoughIPsInPoolException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Not enough IPs found: " + e.getMessage());
		} catch (PoolNameNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Pool Name not found: " + e.getMessage());
		}
		
	}
	
	private HashMap<String,ArrayList<String>> getIpsForMacros (VEEReplica replica)
	{
		ArrayList<String> ipsvm = new ArrayList ();
		HashMap<String,ArrayList<String>> map = new HashMap();
		
		for (NIC nic: replica.getNICs())
		{
			if (nic.getIPAddresses() == null || nic.getIPAddresses().size()==0)
				continue;
			
		
			String ipstring = nic.getIPAddresses().get(0);

			NICConf conf = nic.getNICConf();
			String networkname = conf.getNetwork().getName();
			System.out.println ("Nombre " + networkname+ " ips " + ipstring);
			ipsvm.add(ipstring);
			map.put(networkname, ipsvm);

		}
		return map;
	}
	
	private HashMap<String, HashMap<String, String> >  getEntryPoints(VEEReplica replica)
	{
		HashMap<String, HashMap<String, String> >  entry = new HashMap();

		
		
		

		
		List<String> networks = getNetworksVEE (replica.getVEE());
		HashMap<String, String> netwoksips = null;
		
		for (String net: networks)
		{
			netwoksips = getIpsNetwork (replica.getVEE().getServiceApplication(), net);
			entry.put(net, netwoksips);
			System.out.println ("adding " + net + " " + netwoksips + " for replica" + replica.getVEE().getVEEName());
		}
		return entry;
	}
	


}
