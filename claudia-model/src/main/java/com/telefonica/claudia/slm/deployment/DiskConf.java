/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.deployment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.telefonica.claudia.slm.monitoring.MeasurableElement.MeasureDescriptor;
import com.telefonica.claudia.slm.naming.FQN;

@Entity
public class DiskConf extends HWComponent {
	
	/** 
	 * Capacity units in MegaBytes (MB) 
	 */
    private double capacity = 0;  
    
    @Transient
    private URL imageURL = null;
    private String imageURLString = null;

	
    @Transient
    private File fileSystem = null;
    private String fileSystemPath = null;
    private String digest = null;
    
    public DiskConf() {
        this.setType(HWType.DISK);
    }
    
    public DiskConf(double capacity, URL imageURL, File fileSystem) {
        this.fileSystem = fileSystem;
        this.fileSystemPath = fileSystem.getPath();
        this.imageURL = imageURL;
        this.imageURLString = imageURL.toExternalForm();
        this.capacity = capacity;
        this.setType(HWType.DISK);
    }
    
    @PostLoad
    public void prepareDiskConf() {
    	if (fileSystemPath!= null)
    		this.fileSystem = new File(fileSystemPath);
    	if (imageURLString!=null)
			try {
				this.imageURL = new URL(imageURLString);
			} catch (MalformedURLException e) {
				this.imageURL = null;
			}
    }
    
    public double getCapacity() {
        return capacity;
    }
    
    public void setCapacity(long size){
        this.capacity = size;
    }
    
    public String getDigest() {
    	return digest;
    }
    
    public void setDigest(String digest) {
    	this.digest = digest;
    }
    
    public File getFileSystem() {
        return fileSystem;
    }
    
    public void setFileSystem(File fileSystem) {
        this.fileSystem = fileSystem;
        this.fileSystemPath = fileSystem.getPath();
    }
    
    public URL getImageURL() {
        return imageURL;
    }
    
    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
        this.imageURLString = imageURL.toExternalForm();
    }
    
	public MeasureDescriptor calculateMeasureDescriptorValues(FQN element, String measureId) {
		
		MeasureDescriptor result = new MeasureDescriptor();

		result.typeId=  "diskUsage";
		result.description=  "MB of disk used";
		result.valueType= "xs:decimal";
		result.min=  "0";
		result.max=  String.valueOf(getCapacity());
		
		return result;
	}
}
