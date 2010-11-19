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
package com.telefonica.claudia.slm.lifecyclemanager;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

import org.hibernate.annotations.CollectionOfElements;

/**
 * Network range that uses a fixed list of IPs instead of a networ range
 * to make subnets. The network leasear has to be created as a normal
 * network range, and the IP pool should be populated one by one.
 * 
 */
@Entity
public class NetworkLeaser extends NetworkRange {
	
	@CollectionOfElements
	private Map<String, Boolean> ipPool = new HashMap<String, Boolean>();
	
	public void addIPToPool(String ip) {
		ipPool.put(ip, false);
	}
	
	public String[] getNetwork (long size) {
		if (size < ipPool.size())  {
			return new String[] {ip2String(this.IP), ip2String(mask)}; 
			
		} else
			return null;
	}
	
	public String getNetworkAddress (String networkAddress) {
		for (String ip: ipPool.keySet())
			if (!ipPool.get(ip)) {
				ipPool.put(ip, true);
				return ip;
			}
		
		return null;
	}
	
	/**
	 * Checks whether the given network belong to this leaser. Considering
	 * that leasers do not make subnetting, it will only return true when
	 * the given parameter is exactly the network name of the leaser.
	 * 
	 */
	public boolean isNetworkInRange(String network) {
		if (network.equals(ip2String(this.IP)))
			return true;
		else
			return false;
	}

	/**
	 * Return the given address to the IP pool. Subnet parameter is ignored, but
	 * it is preserved to comply with the interface.
	 */
	public void releaseAddress(String ip, String subnet) {
		ipPool.put(ip, false);
	}
	
	/**
	 * Leasers do not make subnetting, so it doesn't make sense to call this method.
	 * Either way, it's inoffensive.
	 */
	public void releaseSubnet(String ip) {}
	
	public String toString() {
		StringBuffer definition= new StringBuffer();
		
		definition.append("<Leaser Address="+ this.getIP() + ">\n");
		
		for (String ipAddress: ipPool.keySet()) {
			definition.append("<Address IP=\"" + ipAddress + "\" leased=\"" + ipPool.get(ipAddress)+ "\"/>\n");
		}
		
		definition.append("</Leaser>\n");
		
		return definition.toString();
	}
}
