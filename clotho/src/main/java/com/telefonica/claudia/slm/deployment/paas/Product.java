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
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;
import com.telefonica.claudia.slm.naming.ReservoirDirectory;
import com.telefonica.claudia.slm.paas.OVFContextualization;

@Entity
public class Product implements DirectoryEntry,PaaSElement {
    
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
    private String productparent = null;
	
	@Basic
    private String type = null;
	
	@Basic
    private String productUrl = null;
    
	@OneToOne(cascade=CascadeType.ALL)
    private FQN productFQN = null;
    
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private Set<ServiceApplication> services = new HashSet<ServiceApplication>();
    
    @OneToMany(mappedBy="key2", cascade=CascadeType.ALL)
    private Set<Property> properties = new HashSet<Property>();
    
    @OneToMany(mappedBy="productName", cascade=CascadeType.ALL)
    private Set<Product> products = new HashSet<Product>();
    
    
    @ManyToOne
    private VEE vee = null;
    
    @ManyToOne
    private Product parent = null;
    
    public Product() {}
    
    public Product(String productName) {
    	if(productName == null)
    		throw new IllegalArgumentException("Product name cannot be null");
        this.productName = productName;
    }
    
    public String getName(){
        return productName;
    }
    
    public void setName(String name){
         productName = name;
    }
    public String getVersion(){
        return version;
    }
    
    public String getType(){
        return type;
    }
    
    public void setType(String type){
    	this.type = type;
    }
    
    public String getParentName(){
        return productparent;
    }
    
    public void setParentName(String type){
    	productparent = type;
    }
    
    public Product getParent(){
        return parent;
    }
    
    public void setParent(Product parent){
        this.parent = parent;
    }
    
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
        return productUrl;
    }
    
    public void setUrl(String productUrl){
        this.productUrl = productUrl;
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
    
    public Property getPropertyByNameFinished(String prop) {

    	for (Iterator<Property> it = properties.iterator(); it.hasNext(); ) {
    		Property net = it.next();
    		if (net.getKey().endsWith(prop)) {
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
    
    public VEE getVEE ()
    {
    	return vee;
    }
    
    public void setVEE (VEE vee)
    {
    	this.vee = vee;
    }
    
    public Set<Product> getProducts()
    {
    	return products;
    }
    
    public void setProduct (Product childproduct)
    {
    	childproduct.setParent(this);
    	this.products.add(childproduct);
    	
    }
    
    public Product getProductByName(String productName) {

    	for (Iterator<Product> it = products.iterator(); it.hasNext(); ) {
    		Product net = it.next();
    		if (net.getName().equals(productName)) {
    			return net;
    		}
    	}
    	return null;
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
    
	public Product (ProductSectionType productSection)
	{
		if (productSection.getProduct()== null)
		return;
		
		
		if (productSection.getVersion()!=null)
		{
		setVersion(productSection.getVersion().getValue());
		}
		if (productSection.getVendor()!=null)
		{
		setVendor(productSection.getVendor().getValue());
		
		}
		if (productSection.getProductUrl()!=null)
		{
		setUrl(productSection.getProductUrl().getValue());
		
		}
		
		List<Object> sections = productSection.getCategoryOrProperty();
		
		for (Object prop : sections)
		{
			
			if (prop instanceof ProductSectionType.Property)
			{
				ProductSectionType.Property property = (ProductSectionType.Property )prop;
				addProperty(property.getKey(), property.getValue());
				
			}
		}
		
		
	}
	
	public String getProductXML () throws XMLException
	{
		
		String ip = getIpVm (this.getVEE());
	
		return getProductXML(ip);
	}
	
	public String getProductXML (String ip) throws XMLException
	{
		ProductSectionType productsection = new ProductSectionType();
		
		if (getName()!=null)
		{
			MsgType productname = new MsgType();
			productname.setValue(getName());
			productsection.setProduct(productname);
		}
		
		if (getVersion()!=null)
		{
			CimString mens = new CimString();
			mens.setValue(getVersion());
			productsection.setVersion(mens);
		}
		
		if (getVendor()!=null)
		{
			MsgType mens = new MsgType();
			mens.setValue(getVendor());
			productsection.setVendor(mens);
		}
		
		if (getUrl()!=null)
		{
			CimString mens = new CimString();
			mens.setValue(getUrl());
			productsection.setProductUrl(mens);
		}
		
		


		  
		  
		MsgType category  = new MsgType ();
		category.setMsgid("org.fourcaast.instancecomponent");
		category.setValue("Instance Component Metadata");
		productsection.getCategoryOrProperty().add(category);
		
		ProductSectionType.Property property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.instancecomponent.id");
		property.setValue(this.getName());
		productsection.getCategoryOrProperty().add(property);
		
		property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.instancecomponent.type");
		property.setValue(this.getType());
		productsection.getCategoryOrProperty().add(property);
		
		property = new ProductSectionType.Property ();
		property.setKey("org.fourcaast.instancecomponent.recipe");
		property.setValue(this.getName());
		productsection.getCategoryOrProperty().add(property);
		
		if (ip != null)
		{
			property = new ProductSectionType.Property ();
			property.setKey("org.fourcaast.instancecomponent.ip");
			property.setValue(ip);
			productsection.getCategoryOrProperty().add(property);
		}
		if (this.getParentName()!=null)
		{
			property = new ProductSectionType.Property ();
			property.setKey("org.fourcaast.instancecomponent.parent");
			property.setValue(this.getParentName());
			productsection.getCategoryOrProperty().add(property);
		}
		
		category  = new MsgType ();
		category.setMsgid("org.fourcaast.instancecomponent.attributes");
		category.setValue("Product Specific Attributes");
		productsection.getCategoryOrProperty().add(category);
		
		
		
		for (Property prop : getProperties())
		{
			
				property = new ProductSectionType.Property ();
				property.setKey(prop.getKey());
				if (prop.getValue().indexOf("@")!=-1)
				{
					OVFContextualization context = new OVFContextualization ();
					String valuemacro = context.getMacro(this.getVEE(), prop.getValue());
					System.out.println ("Property macro " + prop.getValue() + " " + valuemacro );
					property.setValue(valuemacro);
				}
				else
				   property.setValue(prop.getValue());
				productsection.getCategoryOrProperty().add(property);
		}
		
		
		OVFSerializer ovfSerializer = OVFSerializer.getInstance();
		String ovfproduct = null;
	
		ovfproduct = ovfSerializer.writeXML(productsection);
		return ovfproduct;
	
	}
	
	public String getIpVm (VEE vee)
	{
		// We chose the first one
		for (VEEReplica replica: vee.getVEEReplicas())
		{
			for (NIC nic: replica.getNICs())
			{
				Network net = nic.getNICConf().getNetwork();
				
					return net.getNetworkAddresses()[0];
				
			}
		}
		return null;
	}

	public void setParent(String type) {
		// TODO Auto-generated method stub
		
	}
    
}
