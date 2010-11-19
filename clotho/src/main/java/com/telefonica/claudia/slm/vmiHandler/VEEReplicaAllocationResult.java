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
package com.telefonica.claudia.slm.vmiHandler;

public class VEEReplicaAllocationResult {
	
	private int idInRemoteInfrs = 0;
	private String message = null;
	private boolean success = false; 
	
	public VEEReplicaAllocationResult(int idInRemoteInfrs, String message, boolean success) {
		this.idInRemoteInfrs = idInRemoteInfrs;
		this.message = message;
		this.success = success;
	}

	// If success == true, id is the identier of the vm in the remote infrastructure
	public int getVEEReplicaIdInRemoteInfrastructure() {
		return idInRemoteInfrs;		
	}
	
	// If success == false, message is the error message returned by the infrastructure
	public String getMessage() {
		return message;
	}
	
	public boolean isSuccess() {
		return success;
	}

}
