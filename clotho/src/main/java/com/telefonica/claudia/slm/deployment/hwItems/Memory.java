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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class Memory implements DirectoryEntry {
    
	@Id
	@GeneratedValue
	public long internalId;
	
	@OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private MemoryConf memoryConf = null;
    
    private int freeMem = 0;
    private int cacheMem = 0;
    
    @ManyToOne
    private VEEReplica veeReplica = null;
    
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private FQN memFQN = null;
    
    public Memory() {}
    
    public Memory(MemoryConf memoryconf, VEEReplica veeReplica) {
    	if(memoryconf == null)
    		throw new IllegalArgumentException("Memory conf cannot be null");
    	if(veeReplica == null)
    		throw new IllegalArgumentException("VEE replica cannot be null");
        this.veeReplica = veeReplica;
        this.memoryConf=memoryconf;
        this.freeMem = memoryconf.getCapacity();
    }

    public int getCacheMem() {
        return cacheMem;
    }

    public void setCacheMem(int cacheMem) {
        this.cacheMem = cacheMem;
    }

    public int getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(int freeMem) {
        this.freeMem = freeMem;
    }
    
    public VEEReplica getVEEReplica() {
        return veeReplica;
    }
    
    public MemoryConf getMemoryConf() {
    	return memoryConf;
    }
    
    public FQN getFQN() {
        if(memFQN == null)
        	memFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return memFQN;
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
        
        if(!(object instanceof Memory))
            return false;
                    
        return ((Memory)object).getFQN().equals(getFQN());
    }
}
