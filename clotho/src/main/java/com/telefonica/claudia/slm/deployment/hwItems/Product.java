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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class Product implements DirectoryEntry {
    
	@Id
	@GeneratedValue
	public long internalId;
	
	@Basic
    private String productName = null;
	
	@Basic
    private String version = null;
	
	@Basic
    private String vendor = null;
	
	@Basic
    private String productUrl = null;
    
	@OneToOne(cascade=CascadeType.ALL)
    private FQN productFQN = null;
    
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private Set<ServiceApplication> services = new HashSet<ServiceApplication>();
    
    private HashMap mapProperties = new  HashMap();
    
    @OneToOne(cascade=CascadeType.ALL)
    private VEE vee = null;
    
    public Product() {}
    
    public Product(String productName) {
    	if(productName == null)
    		throw new IllegalArgumentException("Product name cannot be null");
        this.productName = productName;
    }
    
    public String getProductName(){
        return productName;
    }
    
    public String getProductVersion(){
        return version;
    }
    
    public void setProductVersion(String version){
        this.version = version;
    }
    
    public String getProductVendor(){
        return vendor;
    }
    
    public void setProductVendor(String vendor){
        this.vendor = vendor;
    }
    
    public String getProductUrl(){
        return productUrl;
    }
    
    public void setProductUrl(String productUrl){
        this.productUrl = productUrl;
    }
    
    public void addProperty (String key, String value)
    {
    	mapProperties.put(key, value);
    }
    
    public String getProperty (String key)
    {
    	return (String)mapProperties.get(key);
    }
    
    public HashMap<String,String> getProperties ()
    {
    	return mapProperties;
    }
    
    
    public Set<ServiceApplication> getServices() {
        return services;
    }
    
    public void registerService(ServiceApplication service) {
        
        if(service == null)
            throw new IllegalArgumentException("Cannot register null service");
        
        if(!service.getCustomer().equals(this))       
            throw new IllegalArgumentException("Trying to register Service " + service + " on a different customer " + this);
        
        services.add(service);
    }
    
    public VEE getVEE ()
    {
    	return vee;
    }
    
    public void setVEE (VEE vee)
    {
    	this.vee = vee;
    }
    
    
    public boolean isServiceRegistered(ServiceApplication service) {
        return services.contains(service);
    }
    
    public void unregisterService(ServiceApplication service) {
        services.remove(service);
        productFQN.removeChild(service.getFQN());
    }
    
    public FQN getFQN(){
        if(productFQN == null)
            productFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return productFQN;
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
        
        if(!(object instanceof Product))
            return false;
        
        return ((Product)object).getFQN().equals(getFQN());
        
    }
    
    public class Property
    {
    	String key = null;
    	String value = null;
    	
    	 public String getKey(){
    	        return key;
    	 }
    	    
    	 public void setKey(String key){
    	        this.key = key;
    	    }
    	 
    	 public void setValue(String value){
 	        this.value = value;
 	    }
    	 
    	 public String getValue(){
 	        return value;
 	 }
    	
    }
}
