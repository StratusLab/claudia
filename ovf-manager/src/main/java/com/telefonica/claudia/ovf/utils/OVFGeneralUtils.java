/**
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is available at https://svn.forge.morfeo-project.org/claudia
 *
 * The Initial Developer of the Original Code is Telefonica Investigacion y Desarrollo S.A.U.,
 * (http://www.tid.es), Emilio Vargas 6, 28043 Madrid, Spain.
.*
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s): ______________________________________.
 *
 */

/**
 * This class is to hold utilities that does not fit in any other utility class, e.g. OVFEnvelopeUtils,
 * OVFEnvironmentUtils, etc., because they can be used from many places
 */
package com.telefonica.claudia.ovf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abiquo.ovf.OVFEnvironmentUtils;
import com.abiquo.ovf.exceptions.DNSServerNotFoundException;
import com.abiquo.ovf.exceptions.GatewayNotFoundException;
import com.abiquo.ovf.exceptions.IPNotFoundException;
import com.abiquo.ovf.exceptions.NetmaskNotFoundException;
import com.abiquo.ovf.exceptions.NotEnoughIPsInPoolException;
import com.abiquo.ovf.exceptions.PoolNameNotFoundException;
import com.abiquo.ovf.exceptions.PrecedentTierEntryPointNotFoundException;

public class OVFGeneralUtils {
	
	/* String used for macros in macroReplacement method*/
	private final static String IP_MACRO = "IP";
	private final static String ID_MACRO = "id";
	private final static String NETMASK_MACRO = "Netmask";
	private final static String DNSSERVER_MACRO = "DNSServer";
	private final static String GATEWAY_MACRO = "Gateway";
	private final static String SERVICEID_MACRO = "ServiceId";
	private final static String VEEID_MACRO="VmId";
	
	private final static String DOMAIN_MACRO = "Domain";
	private final static String MONITORINGCHANNEL_MACRO = "MonitoringChannel";
	private final static String PRECEDENTTIERENTRYPOINT_MACRO = "PrecedentTierEntryPoint";
	
	private final static String NOIP_TOKEN = "nullIP";
	private final static String NOPRECEDENTTIERENTTYPOINT_TOKEN = "nullEP";
	
	private final static Logger log = LoggerFactory.getLogger(OVFEnvironmentUtils.class);	

	public static String macroReplacement (String value,
			int instanceNumber, 
    		String domain, 
    		String serviceId, 
    		String veeId,
    		String monitoringChannel, 
    		HashMap<String,ArrayList<String>> ips, 
    		HashMap<String, String> netmasks, 
    		HashMap<String, String> dnsServers, 
    		HashMap<String, String> gateways,
    		HashMap<String, HashMap<String, String> > entryPoints,
    		HashMap<String, HashMap<String,String>> registeredAliasIPs) 
    	throws IPNotFoundException,
    		DNSServerNotFoundException,
    		NetmaskNotFoundException,
    		GatewayNotFoundException, 
    		PrecedentTierEntryPointNotFoundException, 
    		NotEnoughIPsInPoolException, 
    		PoolNameNotFoundException {
		
		/* FIXME: the current implementation is limited to just one macro per property value. E.g.,
		 * the following can not be used:
		 * 
		 * ovf:value="http://@IP(net)/app/@ServiceId/"
		 * 
		 * In order to overcome this limitation a sequential processing ('if' after 'if', instead
		 * of using 'else if', could be used)*/		
		
		if (value.contains("@"+IP_MACRO))
		{
			String prefix = "@" + IP_MACRO + "(";	
			String macroValue = value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(")"));

			/* Two possibilities to take into account: @IP(net) or @IP(net,alias). Note that
			 * alias = "" in the case @IP(net) is used */			
			String network;
			String alias = "";
			StringTokenizer macroValueTokenizer = new StringTokenizer(macroValue,",");
			network = macroValueTokenizer.nextToken();
			if (macroValueTokenizer.hasMoreElements()) {
				alias = macroValueTokenizer.nextToken();
			}

			log.debug("IP@ = " + network);
			if (ips != null) {
				/* Is there any IP already used for that alias? In positive case, take it.
				 * In negative case, get one from the pool for that network, then register
				 * it in the alias register hash */
				String ip;
				if ((registeredAliasIPs.get(network) != null) && (registeredAliasIPs.get(network).get(alias)) != null) {
					ip = registeredAliasIPs.get(network).get(alias);
				}
				else {
					ArrayList<String> pool = (ArrayList<String>)ips.get(network);
					if (pool!=null)
					{
						if (pool.size()>0)
						{	
							ip = pool.get(0);
							pool.remove(0);									
						}
						else
						{
							final String msg = "Not enough IP in pool for network '" + network + "'";
							throw new NotEnoughIPsInPoolException(msg);
						}
					}
					else							
					{
						final String msg = "Pool name for network '" + network + "' not found";
						throw new PoolNameNotFoundException(msg);

					}

					/* It has been already created the hashmap for the network? */
					if (registeredAliasIPs.get(network) != null) {
						HashMap<String,String> aliasHm = registeredAliasIPs.get(network);
						aliasHm.put(alias, ip);
						registeredAliasIPs.put(network, aliasHm);
					}
					else {
						/* Create the hashmap fof the network, then add the alias */
						HashMap<String,String> aliasHm = new HashMap<String,String>();
						aliasHm.put(alias, ip);
						registeredAliasIPs.put(network, aliasHm);
					}
				}
				return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), ip);					
			}
			else {
				return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), NOIP_TOKEN);
			}												
		}
		else if (value.contains("@"+ID_MACRO))
		{
			String prefix = "@" + ID_MACRO + "(";
			int offset = Integer.parseInt(value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(")")));
			int index = offset + instanceNumber;
			log.debug("Id@  = " + index);
			return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), String.valueOf(index));
		}
		else if (value.contains("@"+NETMASK_MACRO))
		{
			String prefix = "@" + NETMASK_MACRO + "(";
			String network = value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(")"));
			String mask = netmasks.get(network);
			log.debug("Netmask@  = " + mask);
			if (mask!=null)
			{
				return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), mask);
			}
			else							
			{
				final String msg = "DNSserver for network '" + network + "' not found";
				throw new NetmaskNotFoundException(msg);
			}							
		}						
		else if (value.contains("@"+DNSSERVER_MACRO))
		{
			String prefix = "@" + DNSSERVER_MACRO + "(";
			String network = value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(")"));
			String dnsserver = dnsServers.get(network);
			log.debug("DNSServer@  = " + dnsserver);
			if (dnsserver!=null)
			{
				return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), dnsserver);
			}
			else							
			{
				final String msg = "DNSserver for network '" + network + "' not found";
				throw new DNSServerNotFoundException(msg);
			}							
		}						
		else if (value.contains("@"+GATEWAY_MACRO))
		{
			String prefix = "@" + GATEWAY_MACRO + "(";
			String network = value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(")"));
			String gw = gateways.get(network);
			log.debug("Gateway@  = " + gw);
			if (gw!=null)
			{
				return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), gw);
			}
			else							
			{
				final String msg = "Gateway for network '" + network + "' not found";
				throw new GatewayNotFoundException(msg);
			}							
		}
		else if (value.contains("@"+PRECEDENTTIERENTRYPOINT_MACRO))
		{
			String prefix = "@" + PRECEDENTTIERENTRYPOINT_MACRO + "(";
			String network = value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(","));
			// the +1 is for the ','
			log.info("Data precedent " + prefix + network);
			String vs = value.substring(value.indexOf(prefix)+prefix.length()+1+network.length(),value.indexOf(")"));

			
			if (entryPoints != null) {

				HashMap<String, String> entryPointsPerNetwork = (HashMap<String, String>)entryPoints.get(network);

				if (entryPointsPerNetwork == null) {
					final String msg = "PrecendentTierEntryPoint for network '" + network + "' not found";
					throw new PrecedentTierEntryPointNotFoundException(msg);
				}

				String ep = entryPointsPerNetwork.get(vs);												
				log.debug("PrecendentTierEntryPoint@  = " + ep);
				if (ep!=null)
				{
					return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), ep);
				}
				else							
				{
					final String msg = "PrecendentTierEntryPoint for VirtualSystem '"+vs+"' in network '" + network + "' not found";
					throw new PrecedentTierEntryPointNotFoundException(msg);
				}
			}
			else {
				return value.replace(value.substring(value.indexOf(prefix),value.indexOf(")")+1), NOPRECEDENTTIERENTTYPOINT_TOKEN);
			}

		}
		else if (value.contains("@" + DOMAIN_MACRO))
		{
			String prefix = "@" + DOMAIN_MACRO;
			return value.replace(prefix, domain);
		}
		else if (value.contains("@" + SERVICEID_MACRO))
		{
			String prefix = "@" + SERVICEID_MACRO;
			return value.replace(prefix,serviceId);
		}
		else if (value.contains("@" + VEEID_MACRO))
		{
			String prefix = "@" + VEEID_MACRO;
			System.out.println ("VEE MACRO" + prefix);
			return value.replace(prefix,veeId);
		}
		else if (value.contains("@" + MONITORINGCHANNEL_MACRO))
		{
			String prefix = "@" + MONITORINGCHANNEL_MACRO;
			return value.replace(prefix,monitoringChannel);
		}
		else {
			// Unknown cloudConfigurable @ macro: deal with it in the same way a a
			// no-cloudConfigurable value: i.e. do nothing
			// FIXME: raise exception?
			return value;
		}

	}
	
}
