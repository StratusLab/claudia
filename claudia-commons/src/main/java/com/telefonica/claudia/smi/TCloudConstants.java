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
package com.telefonica.claudia.smi;

public abstract class TCloudConstants {
	
	public static final String TAG_NETWORK_ROOT = "Network";
	public static final String ATTR_NETWORK_NAME = "name";
	
	
	public static final String TAG_NETWORK_NETMASK = "Netmask";
	public static final String TAG_NETWORK_BASE_ADDRESS = "BaseAddress";
	
	public static final String TAG_NETWORK_IPLEASES = "IPLease";
	public static final String TAG_NETWORK_IP = "IP";
	public static final String TAG_NETWORK_MAC = "MAC";
	public static final String TAG_NETWORK_MAC_ENABLED = "MacEnabled";
	public static final String TAG_NETWORK_GATEWAY = "Gateway";
	public static final String TAG_NETWORK_DNS = "DNS";
	
	
	
	
	public static final String TAG_INSTANTIATE_OVF = "InstantiateOvfParams";
	public static final String ATTR_INSTANTIATE_OVF_NAME = "name";
	
	
	public static final String TAG_INSTANTIATION_PARAMS = "InstantiationParams";
	public static final String ATTR_INSTANTIATION_PARAMS_NAME = "name";
	
	public static final String ATTR_NETWORK_TYPE = "NetworkType";
	
	
	public static final String TAG_NETWORK_CONFIG = "NetworkConfig";
	public static final String TAG_NETWORK_CONFIG_SECTION = "NetworkConfigSection";
	public static final String TAG_NETWORK_ASSOCIATION = "NetworkAssociation";
	
	
	public static final String TAG_ASPECTS_SECTION = "AspectsSection";
	public static final String TAG_ASPECT = "Aspect";
	public static final String TAG_ASPECT_PROPERTY = "Property";
	public static final String TAG_ASPECT_KEY = "Key";
	public static final String TAG_ASPECT_VALUE = "Value";
	public static final String ATTR_ASPECT_VSYSTEM="vsystem";
	
	public static final String ATTR_NETWORK_ASSOCIATION_HREF= "href";
	
	public static final String TAG_TASKS="Tasks";
	public static final String TAG_TASK="Task";
	
	public static enum StateType {initial, deploying, active, running, poweredOff, suspended, unactive, error, unknown};
	
}
