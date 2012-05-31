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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CollectionOfElements;

import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class NIC implements DirectoryEntry {    
    
	@Id
	@GeneratedValue
	public long internalId;
	
    private int id = 0;
    private int throughput = 0;


    @CollectionOfElements
    private List<String> ipAddresses = new ArrayList<String>();
    
    private byte[] macAddress = null;
    
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private NICConf nicConf = null;
    
    @ManyToOne
    private VEEReplica veeReplica = null;
    
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private FQN nicFQN = null;
    
    public NIC() {}
    
    public NIC(int id, NICConf nicConf, VEEReplica veeReplica) {
    	if(veeReplica == null)
    		throw new IllegalArgumentException("VEE replica cannot be null");
    	if(nicConf == null)
    		throw new IllegalArgumentException("NIC conf cannot be null");
        this.id = id;
        this.veeReplica = veeReplica;
        this.nicConf = nicConf;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<String> getIPAddresses() {
        return ipAddresses;
    }
    
    public void addIPAddress(String ipAddress) {
        this.ipAddresses.add(ipAddress);
    }
    
    public byte[] getMacAddress() {
    	return macAddress;
    }
    
    public void setMacAddress(byte[] macAddress) {
    	this.macAddress = macAddress;
    }
    
    public int getThroughput() {
        return throughput;
    }
    
    public void setThroughput(int throughput) {
        this.throughput = throughput;                 
    }
    
    public VEEReplica getVEEReplica() {
        return veeReplica;
    }
    
    public NICConf getNICConf() {
    	return nicConf;
    }
    
    public FQN getFQN() {
        if(nicFQN == null)
            nicFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return nicFQN;
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
        
        if(!(object instanceof NIC))
            return false;
                    
        return ((NIC)object).getFQN().equals(getFQN());
    }
}
