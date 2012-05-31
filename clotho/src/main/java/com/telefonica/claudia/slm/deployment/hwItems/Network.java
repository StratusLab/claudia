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
package com.telefonica.claudia.slm.deployment.hwItems;

import java.util.HashMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class Network implements DirectoryEntry {
    
	@Id
	@GeneratedValue
	public long internalId;
	
	/**
	 * Network name.
	 */
    private String name = null;
    
    /**
     * The maximum number of ips that will be leased in this network. Default value
     * is a C class network (the real value should be calculated upon the expected 
     * needs of the Service Application).
     */
    private int size = 255;
    
    /**
     * Indicates whether the hosts in this network will have a public IP or
     * belong to a private network.
     */
    private boolean privateNet = false;
    
	/**
	 * Network addresses asociated to the network. The network addresess
	 * are given by the Lifecycle controller from one of its IP ranges. The value is an array
	 * containing, in this order: 
	 * 		- The IP address of the network.
	 * 		- The Network mask
	 * 		- The DNS Server if there is one, or null, otherwise
	 * 		- The Gateway if there is one, or null otherwise;
	 * 
	 */
	private String[] networkAddresses;
    
	/**
	 * Entrypoints inside each network. Each entrypoint is represented as
	 * a pair <VEEName, IPAddress>. This array contains the last IP Address leased to 
	 * each VEE: it will only make sense in the entrypoint VEES, that will have only
	 * one replica per VEE.
	 */
	private HashMap<String, String> entryPoints= new  HashMap<String, String>();
	
	/**
	 * Identifier of the network in the underlying infrastructure.
	 */
	private int infrastructureId;
    
    private String bridge = null;
	
    @ManyToOne
    private ServiceApplication serviceApplication = null;
    
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private FQN networkFQN = null;
    
    public Network() {}
    
    public Network(String name, ServiceApplication serviceApplication) {
    	if(serviceApplication == null)
    		throw new IllegalArgumentException("Service application cannot be null");
        this.name = name;
        this.serviceApplication = serviceApplication;
    }
    
    public String getName() {
        return name;
    }
    
    public void setNetworkAddresses(String[] addresses) {
    	this.networkAddresses = addresses;
    }
    
    public String[] getNetworkAddresses() {
    	return networkAddresses;
    }
    
    public HashMap<String, String> getEntryPoints() {
    	return entryPoints;
    }
    
    public ServiceApplication getServiceApplication() {
        return serviceApplication;
    }
    
    public void setSize(int s) {
    	this.size = s;
    }
    
    public int getSize() {
    	return size;
    }
    
	public String getBrigde() {
		return bridge;
	}
	
	public void setBrigde(String bridge) {
		this.bridge = bridge;
	}
    
    public void setInfrastructureId(int id) {
    	this.infrastructureId = id;
    }
    
    public int getInfrastructureId() {
    	return this.infrastructureId;
    }
    
    public void setPrivateNet(boolean p) {
    	this.privateNet = p;
    	if (p)
			this.bridge= "private";
		else
			this.bridge= "public";
    }
    
    public boolean getPrivateNet() {
    	return privateNet;
    }
    
    public FQN getFQN() {
    	if(networkFQN == null)
    		networkFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return networkFQN;
    }
    
    public void setFQN(String fqn) {
    	networkFQN = new FQN (fqn);
    }
    
    @Override
    public String toString() {
    	return getFQN().toString();
    }
    
    @Override
    public int hashCode() {
    	return getFQN().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
    	
    	if(object == null)
    		return false;
    	
    	if(!(object instanceof Network))
    		return false;
    		
    	return ((Network)object).getFQN().equals(getFQN());
    	
    }
}
