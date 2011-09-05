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
package com.telefonica.claudia.slm.deployment.paas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;
import org.hibernate.annotations.CollectionOfElements;

import com.abiquo.ovf.exceptions.XMLException;
import com.abiquo.ovf.xml.OVFSerializer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;

@Entity
public class Application implements DirectoryEntry, PaaSElement {
    
	@Id
	@GeneratedValue
	public long internalId;
	
	@Basic
    private String applicationName = null;
	
	@Basic
    private String version = null;
	
	@Basic
    private String vendor = null;
	
//	@Basic
//    private String applicationType = null;
	
	@Basic
    private String applicationUrl = null;
    
	@OneToOne(cascade=CascadeType.ALL)
    private FQN applicationFQN = null;
    
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private Set<ServiceApplication> services = new HashSet<ServiceApplication>();
    
    @OneToMany(mappedBy="key2", cascade=CascadeType.ALL)
    private Set<Property> properties = new HashSet<Property>();
    
 //   @OneToMany(mappedBy="applicationName", cascade=CascadeType.ALL)
 //   private Set<application> applications = new HashSet<application>();
   // private HashMap mapProperties = new  HashMap();
    
    @ManyToOne
    private Product product = null;
    
    public Application() {}
    
    public Application(String applicationName) {
    	if(applicationName == null)
    		throw new IllegalArgumentException("application name cannot be null");
        this.applicationName = applicationName;
    }
    
    public String getName(){
        return applicationName;
    }
    
    public void setName(String name){
         applicationName = name;
    }
    public String getVersion(){
        return version;
    }
    
/*    public String getApplicationType(){
        return applicationType;
    }
    
    public void setApplicationType(String type){
    	applicationType = type;
    }*/
    
    public void setVersion(String version){
        this.version = version;
    }
    
    public String getVendor(){
        return vendor;
    }
    
    public void setVendor(String vendor){
        this.vendor = vendor;
    }
    
    public String getUrl(){
        return applicationUrl;
    }
    
    public void setUrl(String applicationUrl){
        this.applicationUrl = applicationUrl;
    }
    
    public void addProperty (Property property)
    {
    	properties.add(property);
    }
    
    public void addProperty (String key, String value )
    {
    	Property prop = new Property (key,value);
    	properties.add(prop);
    }
    
    public Property getPropertyByName(String netName) {

    	for (Iterator<Property> it = properties.iterator(); it.hasNext(); ) {
    		Property net = it.next();
    		if (net.getKey().equals(netName)) {
    			return net;
    		}
    	}
    	return null;
    }
    
   
    
    public Set<Property> getProperties ()
    {
    	return properties;
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
    
    public Product getProduct ()
    {
    	return product;
    }
    
    public void setProduct (Product product)
    {
    	this.product = product;
    }
    
  /*  public Set<application> getApplications()
    {
    	return applications;
    }
    
    public void setApplication (application application)
    {
    	this.applications.add(application);
    }
    
    public application getApplicationByName(String applicationName) {

    	for (Iterator<application> it = applications.iterator(); it.hasNext(); ) {
    		application net = it.next();
    		if (net.getApplicationName().equals(applicationName)) {
    			return net;
    		}
    	}
    	return null;
    }*/
    
    
    public boolean isServiceRegistered(ServiceApplication service) {
        return services.contains(service);
    }
    
    public void unregisterService(ServiceApplication service) {
        services.remove(service);
        applicationFQN.removeChild(service.getFQN());
    }
    
    public FQN getFQN(){
        if(applicationFQN == null)
            applicationFQN = ReservoirDirectory.getInstance().buildFQN(this);
        return applicationFQN;
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
        
        if(!(object instanceof Application))
            return false;
        
        return ((Application)object).getFQN().equals(getFQN());
        
    }
    
	public Application (ProductSectionType applicationSection)
	{
		if (applicationSection.getProduct()== null)
		return;
		
		
		if (applicationSection.getVersion()!=null)
		{
		setVersion(applicationSection.getVersion().getValue());
		}
		if (applicationSection.getVendor()!=null)
		{
		setVendor(applicationSection.getVendor().getValue());
		
		}
		if (applicationSection.getProductUrl()!=null)
		{
		setUrl(applicationSection.getProductUrl().getValue());
		
		}
		
		List<Object> sections = applicationSection.getCategoryOrProperty();
		
		for (Object prop : sections)
		{
			
			if (prop instanceof ProductSectionType.Property)
			{
				ProductSectionType.Property property = (ProductSectionType.Property )prop;
				addProperty(property.getKey(), property.getValue());
				
			}
		}
		
		
	}
	
	public String getApplicationXML () throws XMLException
	{
		ProductSectionType applicationsection = new ProductSectionType();
		
		if (getName()!=null)
		{
			MsgType applicationname = new MsgType();
			applicationname.setValue(getName());
			applicationsection.setProduct(applicationname);
		}
		
		if (getVersion()!=null)
		{
			CimString mens = new CimString();
			mens.setValue(getVersion());
			applicationsection.setVersion(mens);
		}
		
		if (getVendor()!=null)
		{
			MsgType mens = new MsgType();
			mens.setValue(getVendor());
			applicationsection.setVendor(mens);
		}
		
		if (getUrl()!=null)
		{
			CimString mens = new CimString();
			mens.setValue(getUrl());
			applicationsection.setProductUrl(mens);
		}
		
		for (Property prop : getProperties())
		{
			
				ProductSectionType.Property property = new ProductSectionType.Property ();
				property.setKey(prop.getKey());
				property.setValue(prop.getValue());
				applicationsection.getCategoryOrProperty().add(property);
		}
		
		
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		String ovfapplication = null;
	
		ovfapplication = ovfSerializer.writeXML(applicationsection);
		return ovfapplication;
	
	}

	public Product getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setParent(String type) {
		// TODO Auto-generated method stub
		
	}


    
}
