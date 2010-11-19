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
package com.telefonica.claudia.smi.provisioning;

import java.io.IOException;

/**

 * 
 * All the drivers implementing DeploymentDriver must have a one-argument constructor that
 * takes a single Properties parameter.
 * 
 */
public interface ProvisioningDriver {
	
	/**
	 * Deploys the new VM represented by the data in the OVF with its fqn as the name.
	 * 
	 * @param data
	 * 		OVF representation of the Virtual Machine to be deployed. The deployment is
	 * 		delayed, so the user should ask the task manager to check its result.
	 * 
	 * @throws IOException
	 */
	public long deployVirtualMachine(String fqn, String ovf) throws IOException;

	/**
	 * Returns an object representing the actual state of the selected VM. 
	 * 
	 * @param fqn
	 * 		FQN of the VM in the SLM.
	 * 
	 * @return
	 * 		OVF representation of the actual state of the selected Virtual Machine.
	 * 		
	 * @throws IOException
	 */
	public String getVirtualMachine(String fqn) throws IOException;
	
	/**
	 * Shut down the selected VM and remove it from the server.
	 * 
	 * @param fqn
	 * 		FQN of the VM in the SLM.
	 * 
	 * @throws IOException
	 */
	public long deleteVirtualMachine(String fqn) throws IOException;
	
	/**
	 * Creates a new network resource. The deployment is
	 * delayed, so the user should ask the task manager to check its result.
	 * 
	 * @throws IOException
	 */
	public long deployNetwork(String netFqn, String ovf) throws IOException;
	
	/**
	 * Gets information about the selected network resource.
	 * 
	 * @return
	 * 		OVF Network representation of the actual state of the selected network.
	 * 
	 * @throws IOException
	 */
	public String getNetwork(String fqn) throws IOException;
	
	public String getNetworkList() throws IOException;
	
	/**
	 * Remove the selected virtual network.
	 * 
	 * @param fqn
	 * 		FQN of the Network.
	 * 
	 * @throws IOException
	 */
	public long deleteNetwork(String fqn) throws IOException;
	
	/**
	 * Perform a power action on a VApp.  If it has child VApps, this
	 * action will affect to all of them.
	 * 
	 * @param fqn
	 * 		FQN of the VApp.
	 * @param action
	 * 		One of powerOn, powerOff, reset, suspend, shutdown or reboot
	 * 
	 * @throws IOException
	 */
	public long powerActionVirtualMachine(String fqn, String action) throws IOException;
}
