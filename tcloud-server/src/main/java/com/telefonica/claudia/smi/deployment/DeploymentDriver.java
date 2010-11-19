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
package com.telefonica.claudia.smi.deployment;

import java.io.IOException;

/**
 * This interface should be implemented when creating a new Service Lifecycle Manager driver;
 * it provides all the functionalities needed to manage a Service Lifecycle.
 * 
 * All the drivers implementing DeploymentDriver must have a one-argument constructor that
 * takes a single Properties parameter.
 * 
 */
public interface DeploymentDriver {

    /**
     * Deploys a service application for the customer passed as a parameter.
     * 
     * @param fqnCustomer
     * 		Fully qualified name of the customer whose service is to be deployed.
     *      
     * @param serviceName 
     * 		Name for the service to be deployed.
     * 
     * @param ovf
     * 		String with a XML representation of the service in OVF format.
     * 
     * @return long 
     * 		Task id of the deployment action.
     * 
     * @throws IOException 
     */ 
	public long deploy(String fqnCustomer, String serviceName, String ovf) throws IOException;
	
    /** 
     * Retrieves all the information related to the service from the Service Manager.
    * 
    * @param serviceId
    * 		FQN of the service.
    * 
    * @return
    * 		XML description of the service.
    * 
    * @throws IOException
    */
	public String getService(String fqn) throws IOException;
	
    /** 
     * Retrieves all the information related to the VEE from the Service Manager.
    * 
    * @param veeId
    * 		FQN of the VEE.
    * 
    * @return
    * 		XML description of the service.
    * 
    * @throws IOException
    */
	public String getVee(String fqn) throws IOException;
	
	/**
	 * Retrieves information on the selected VDC. 
	 * 
	 * @param fqn
	 * 		FQN of the service to retrieve.
	 * 
	 * @return
	 * 		XML Representation of the VDC
	 * 
	 * @throws IOException
	 */
	public String getVdc(String fqn) throws IOException;
	
	/**
	 * Creates a new VDC. 
	 * 
	 * @param fqnCustomer
	 * 		Fully qualified name of the customer whose VDC is to be deployed.
	 * 
     * @param vdc
     * 		String with a XML representation of the VDC.
     * 
     * @return long 
     * 		Task id of the create action.
	 * 
	 * @throws IOException
	 */
	public long createVdc(String fqnCustomer, String vdc) throws IOException;
	
	/**
	 * Retrieves information on the selected org. 
	 * 
	 * @param fqn
	 * 		FQN of the service to retrieve.
	 * 
	 * @return
	 * 		XML Representation of the org
	 * 
	 * @throws IOException
	 */
	public String getOrg(String fqn) throws IOException;
	
    /**
     * Undeploy the selected service.
     * 
     * @param receives 
     * 		A String representation of the service's FQN.
     * 
     * @return
     * 		Task id of the undeployment action.
     */
	public void undeploy(String fqn) throws IOException;
}
