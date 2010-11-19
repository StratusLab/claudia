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

package com.telefonica.claudia.slm.deployment;

import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

public class ProbeKPI implements DirectoryEntry {

	public long internalId;
	
    private ServiceApplication serviceApplication = null;
    
    private String kpiName = null;
    private double kpiValue = 0.0;
    
    private FQN kpiFQN = null;
    
    public ProbeKPI() {}
    
    public ProbeKPI(ServiceApplication serviceApplication, String kpiName) {
    	if(serviceApplication == null)
    		throw new IllegalArgumentException("Service application cannot be null");
    	if(kpiName == null)
    		throw new IllegalArgumentException("KPI name cannot be null");
    	this.serviceApplication = serviceApplication;
    	this.kpiName = kpiName;
        
    }
    
    public String getKPIName() {
    	return kpiName;
    }
    
    public void setKPIValue(double kpiValue) {
    	this.kpiValue = kpiValue;
    }
    
    public double getKPIValue() {
    	return kpiValue;
    }
    
    public ServiceApplication getServiceApplication() {
    	return serviceApplication;
    }
    
    public FQN getFQN() {
    	if(kpiFQN == null)
    		kpiFQN = ReservoirDirectory.getInstance().buildFQN(this);
    	return kpiFQN;
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
    	
    	if(!(object instanceof ServiceKPI))
    		return false;
    	
    	return ((ServiceKPI)object).getFQN().equals(getFQN());
    	
    }
}
