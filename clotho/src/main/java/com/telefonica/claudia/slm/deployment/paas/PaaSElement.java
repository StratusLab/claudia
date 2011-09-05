package com.telefonica.claudia.slm.deployment.paas;

import java.util.Iterator;

public interface PaaSElement {

    public Product getParent();
    
    public void setParent(String type);
    
    public void setVersion(String version);
    
    public String getVendor();
    
    public void setVendor(String vendor);
    
    public String getUrl();
    
    public void setUrl(String productUrl);
    
    public void addProperty (Property property);
    
    public void addProperty (String key, String value );
    
    public Property getPropertyByName(String netName);
}
