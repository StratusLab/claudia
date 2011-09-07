package com.telefonica.claudia.slm.paas;

import com.telefonica.claudia.slm.common.SMConfiguration;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.Network;

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
