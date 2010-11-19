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

import java.util.ArrayList;

public class URICreation {

	public static final String VDC_MIME_TYPE = "application/vnd.telefonica.tcloud.vdc+xml";
	public static final String VAPP_MIME_TYPE = "application/vnd.telefonica.tcloud.vapp+xml";
	
	public static final String URI_API = "/api";
	public static final String URI_ORG = URI_API + "/org/{org-id}";
	
	public static final String URI_VDC_ROOT = URI_ORG + "/vdc";
	public static final String URI_VDC = URI_VDC_ROOT + "/{vdc-id}";
	
	public static final String URI_VAPP  = URI_VDC   + "/vapp/{vapp-id}";
	public static final String URI_VAPP2 = URI_VAPP  + "/{vee-id}";
		
	public static final String URI_VAPP3_ADD = URI_VAPP2 + "/action/instantiateOvf";
	public static final String URI_VAPP3 = URI_VAPP2 + "/{vm-id}";
	public static final String URI_VAPP3_POWER = URI_VAPP3 + "/action/power/{power-action}";
	
	public static final String URI_HW_ROOT = URI_VAPP + "/hw";
	public static final String URI_HW = "/hw/{hwitem-id}";
	
	public static final String URI_HWIT  = URI_VAPP  + URI_HW;
	public static final String URI_HWIT2 = URI_VAPP2 + URI_HW;
	public static final String URI_HWIT3 = URI_VAPP3 + URI_HW;
		
	public static final String URI_TASK  = URI_VDC   + "/task/{task-id}";
	
	public static final String URI_NET_ROOT = URI_VAPP + "/net";
	public static final String URI_NET = URI_NET_ROOT + "/{net-id}";
	public static final String URI_NET_ADD = URI_NET_ROOT + "/action/add";

	public static final String MONITOR = "/monitor";
	public static final String URI_VDC_MON  =  URI_VDC +   MONITOR;
	
	public static final String URI_VAPP_MON =  URI_VAPP +  MONITOR;
	public static final String URI_VAPP2_MON = URI_VAPP2 + MONITOR;
	public static final String URI_VAPP3_MON = URI_VAPP3 + MONITOR;
	
	public static final String URI_HWIT_MON  = URI_HWIT  + MONITOR;
	public static final String URI_HWIT2_MON = URI_HWIT2 + MONITOR;
	public static final String URI_HWIT3_MON = URI_HWIT3 + MONITOR;
	
	public static final String URI_NET_MON =   URI_NET +   MONITOR;
	
	public static final String MEASURE_ID = "/{measure-id}";
	public static final String VALUES = "/values";	
	
	public static final String FQN_SEPARATOR_REPLICA = "replicas";
	public static final String FQN_SEPARATOR_VEE = "vees";
	public static final String FQN_SEPARATOR_SERVICE = "services";
	public static final String FQN_SEPARATOR_VDC = "customers";
	public static final String FQN_SEPARATOR_NET = "networks";
	
	public static String getFQN(String org) {
		return org.replace("_", ".");
	}
	
	public static String getFQN(String org, String vdc) {
		return org.replace("_", ".") + "." + FQN_SEPARATOR_VDC + "." + vdc;
	}
	
	public static String getFQN(String org, String vdc, String vapp) {
		return getFQN(org, vdc) + "." + FQN_SEPARATOR_SERVICE + "." + vapp;
	}
	
	public static String getFQN(String org, String vdc, String vapp, String vee) {
		return getFQN(org, vdc, vapp) + "."+ FQN_SEPARATOR_VEE +"." + vee;
	}
	
	public static String getFQN(String org, String vdc, String vapp, String vee, String vm) {
		return getFQN(org, vdc, vapp, vee) + "." + FQN_SEPARATOR_REPLICA +  "." + vm;
	}	
	
	public static String getFQN(String org, String vdc, ArrayList<String> vappList) {
		switch(vappList.size()){
		case 1:
			return getFQN(org, vdc, vappList.get(0));
		case 2:
			return getFQN(org, vdc, vappList.get(0), vappList.get(1));
		case 3:
			return getFQN(org, vdc, vappList.get(0), vappList.get(1), vappList.get(2));
		}
		return null;
	}
	
	public static String getNetworkFQN(String org, String vdc, String service, String network) {
		return getFQN(org, vdc, service) + "." + FQN_SEPARATOR_NET + "." + network;
	}
	
	public static String getHwItemFQN(String org, String vdc, ArrayList<String> vappList, String hwItem) {
		return getFQN(org, vdc, vappList) + "." + hwItem;
	}
	
	public static String getNetworkFQN(String org, String vdc, String network) {
		return getFQN(org, vdc) + "." + network;
	}
	
	public static String getVDC(String fqn) {
		
		if (fqn.indexOf(FQN_SEPARATOR_VDC)<=0) {
			throw new IllegalArgumentException("FQN not well formed or do not contain an VDC");
		}
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        String organization = fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
        
		return getFQN(organization, customer);
	}
	
	public static String getService(String fqn) {
		if (fqn.indexOf(FQN_SEPARATOR_SERVICE)<=0) {
			throw new IllegalArgumentException("FQN not well formed or do not contain an VDC");
		}
		
        String service = fqn.substring(fqn.indexOf(FQN_SEPARATOR_SERVICE));
        String parts[] = service.split("\\.");
        service = parts[1];
        
        String rest = fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_SERVICE)-1);
        
		return rest + "." + FQN_SEPARATOR_SERVICE + "." + service;
	}
	
	public static String getURIVDC(String fqn) {
		
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
		
		return URI_VDC.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer);
	}

	public static String getURIService(String fqn) {
		
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        String service = fqn.substring(fqn.indexOf(FQN_SEPARATOR_SERVICE));
        parts = service.split("\\.");
        service = parts[1];
		
		return URI_VAPP.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer).replace("{vapp-id}", service);
	}
	
	public static String getURIVapp(String fqn) {
		
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
		
        String service = fqn.substring(fqn.indexOf(FQN_SEPARATOR_SERVICE));
        parts = service.split("\\.");
        service = parts[1];
        
        String vee = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VEE));
        parts = vee.split("\\.");
        vee = parts[1];
        
		return URI_VAPP2.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer).replace("{vapp-id}", service).replace("{vee-id}", vee);
	}

	public static String getURIVEEReplica(String fqnReplica) {
		
		String uriService = getURIVapp(fqnReplica);
        
        String veeReplica = fqnReplica.substring(fqnReplica.indexOf(FQN_SEPARATOR_REPLICA));
        String[]  parts = veeReplica.split("\\.");
        veeReplica = parts[1];
		
		return uriService + "/" + veeReplica; 
	}
	
	public static String getURINet(String fqn) {
		
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        String service = fqn.substring(fqn.indexOf(FQN_SEPARATOR_SERVICE));
        parts = service.split("\\.");
        service = parts[1];
        
        String net = fqn.substring(fqn.indexOf(FQN_SEPARATOR_NET));
        parts = net.split("\\.");
        net = parts[1];
        
		return URI_NET.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer).replace("{net-id}", net).replace("{vapp-id}", service);
	}
	
	public static String getURINetAdd(String fqn) {
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        String service = fqn.substring(fqn.indexOf(FQN_SEPARATOR_SERVICE));
        parts = service.split("\\.");
        service = parts[1];
        
		return URI_NET_ADD.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer).replace("{vapp-id}", service);
	}
}
