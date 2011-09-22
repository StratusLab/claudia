package com.telefonica.claudia.slm.paas;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.paas.Product;
import com.telefonica.claudia.slm.deployment.paas.Property;

public class OVFContextualization {
	
	//
	

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

}
