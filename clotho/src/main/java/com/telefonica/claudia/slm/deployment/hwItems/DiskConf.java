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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

@Entity
public class DiskConf {
    
	@Id
	@GeneratedValue
	public long internalId;
	
	/** 
	 * Capacity units in MegaBytes (MB) 
	 */
    private int capacity = 0;  
    
    @Transient
    private URL imageURL = null;
    private String imageURLString = null;

	
    @Transient
    private File fileSystem = null;
    private String fileSystemPath = null;
    
    private String type = null;
    private String digest = null;
    
    public DiskConf() {}
    
    public DiskConf(int capacity, URL imageURL, File fileSystem) {
        this.fileSystem = fileSystem;
        this.fileSystemPath = fileSystem.getPath();
        this.imageURL = imageURL;
        this.imageURLString = imageURL.toExternalForm();
        this.capacity = capacity;
    }
    
    public DiskConf(int capacity, File fileSystem) {
        this.fileSystem = fileSystem;
        this.fileSystemPath = fileSystem.getPath();
        this.capacity = capacity;
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
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int size){
        this.capacity = size;
    }
    
    public String getDigest() {
    	return digest;
    }
    
    public void setDigest(String digest) {
    	this.digest = digest;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type){
        this.type = type;
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
    
}