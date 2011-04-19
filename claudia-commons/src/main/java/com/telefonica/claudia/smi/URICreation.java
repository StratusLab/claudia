/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi;

import java.util.List;

public class URICreation {

	public enum HardwareTypeEnum { disks, cpus, memory, networks };
	
	public static final String VDC_MIME_TYPE = "application/vnd.telefonica.tcloud.vdc+xml";
	public static final String VAPP_MIME_TYPE = "application/vnd.telefonica.tcloud.vapp+xml";
	public static final String ORG_MIME_TYPE = "application/vnd.telefonica.tcloud.org+xml";
	public static final String VAPPLIST_MIME_TYPE = "application/vnd.telefonica.vappList+xml"; 
	public static final String SNAPSHOT_MIME_TYPE = "application/vnd.telefonica.tcloud.snapshot+xml";
	public static final String SNAPSHOTLIST_MIME_TYPE = "application/vnd.telefonica.tcloud.snapshotList+xml";
	
	public static final String URI_MONITORING_CALLBACK = "/api/callback/monitoring";
	
	public static final String URI_API = "/api";
	public static final String URI_ORG = URI_API + "/org/{org-id}";
	public static final String URI_VDC_ADD_MODIFIER = "/action/instantiateVdc";
	public static final String URI_VDC_ADD = URI_ORG + URI_VDC_ADD_MODIFIER;
	
	public static final String URI_HEARTBEAT = URI_API + "/heartbeat";
	
	public static final String URI_POWER_MODIFIER = "/power/action/{power-action}";
	
	public static final String URI_VDC_ROOT = URI_ORG + "/vdc";
	public static final String URI_VDC = URI_VDC_ROOT + "/{vdc-id}";
	public static final String URI_VAPP_LIST = URI_VDC + "/vappList";
	
	public static final String URI_TICKET = URI_API + "/console/ticket";
	
	public static final String URI_ADD_MODIFIER = "/action/instantiateOvf";
	public static final String URI_CLONE_MODIFIER = "/action/clone";
	
	public static final String URI_VAPP  = URI_VDC   + "/vapp/{vapp-id}";
	public static final String URI_VAPP_ADD = URI_VDC + URI_ADD_MODIFIER;	
	public static final String URI_VAPP2 = URI_VAPP  + "/{vee-id}";
		
	public static final String URI_VAPP3_ADD = URI_VAPP2 + URI_ADD_MODIFIER;
	public static final String URI_VAPP3 = URI_VAPP2 + "/{vm-id}";
	public static final String URI_VAPP3_POWER = URI_VAPP3 + URI_POWER_MODIFIER;
	public static final String URI_VAPP3_CLONE = URI_VAPP3 + URI_CLONE_MODIFIER;
	
	public static final String URI_HW_ROOT = URI_VAPP + "/hw";
	public static final String URI_HW = "/hw/{hwitem-id}";
	
	public static final String URI_HWIT  = URI_VAPP  + URI_HW;
	public static final String URI_HWIT2 = URI_VAPP2 + URI_HW;
	public static final String URI_HWIT3 = URI_VAPP3 + URI_HW;
		
	public static final String URI_TASK  = URI_ORG   + "/task/{task-id}";
	
	public static final String URI_NET_ROOT = URI_VDC + "/net";
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
	
	public static final String SNAPSHOT = "/snapshot";
	public static final String URI_SNAPSHOT_ADD     = URI_VAPP3 + SNAPSHOT;
	public static final String URI_SNAPSHOT         = URI_VAPP3 + SNAPSHOT + "/{snapshot-id}";
	public static final String URI_SNAPSHOT_RESTORE = URI_SNAPSHOT + "/action/restore";
	
	public static final String MEASURE_ID = "/{measure-id}";
	public static final String VALUES = "/values";	
	
	public static final String CATALOG = "/catalog";
	public static final String URI_CATALOG_ROOT = URI_API + CATALOG;
	public static final String URI_CATALOGID = URI_CATALOG_ROOT + "/{catalog-id}";
	
	public static final String TEMPLATE = "/template";
	public static final String URI_TEMPLATE_ROOT = URI_CATALOG_ROOT + TEMPLATE;
	public static final String URI_TEMPLATEID = URI_TEMPLATE_ROOT + "/{template-id}";
	public static final String URI_TEMPLATE_ADD = URI_VAPP3 + "/action/templatize";	
	
	public static final String FQN_SEPARATOR_REPLICA = "replicas";
	public static final String FQN_SEPARATOR_VEE = "vees";
	public static final String FQN_SEPARATOR_SERVICE = "services";
	public static final String FQN_SEPARATOR_VDC = "customers";
	public static final String FQN_SEPARATOR_NET = "networks";
	public static final String FQN_SEPARATOR_HW = "hw";
	
	public static final String FQN_SEPARATOR_SNAPSHOT = "snapshots";
	public static final String FQN_SEPARATOR_MEASURE = "measure";
	
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
	
	public static String getFQN(String org, String vdc, List<String> vappList) {
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
	
//	public static String getFQN(String org, String vdc, List<String> vappList, String hwItem) {
//		return getFQN(org, vdc, vappList) + "." + hwItem.substring(0, hwItem.indexOf("_")) + "." + hwItem.substring(hwItem.indexOf("_")+1);
//	}
	
	public static String getFQN(String org, String vdc, List<String> vappList, String hwItem) {
		return getFQN(org, vdc, vappList) + "." + FQN_SEPARATOR_HW + "." + hwItem;
	}
	
	public static String getNetworkFQN(String org, String vdc, String service, String network) {
		return getFQN(org, vdc, service) + "." + FQN_SEPARATOR_NET + "." + network;
	}
	
	public static String getHwItemFQN(String org, String vdc, List<String> vappList, String hwItem, HardwareTypeEnum hwType) {
		return getFQN(org, vdc, vappList) + "." + hwType.name().toLowerCase() + "." + hwItem;
	}
	
	public static String getNetworkFQN(String org, String vdc, String network) {
		return getFQN(org, vdc) + "." + FQN_SEPARATOR_NET  + "." + network;
	}
	
	public static String getSnapshotFQN(String org, String vdc, List<String> vappList, String snapshot) {
		return getFQN(org, vdc, vappList) + "." + FQN_SEPARATOR_SNAPSHOT + "." + snapshot;
	}
	
	public static String getOrg(String fqn) {
		if (fqn.indexOf(FQN_SEPARATOR_VDC)<=0) {
			return fqn;
		}

		return fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
	}
	
	public static String getVDC(String fqn) {
		
		if (fqn.indexOf(FQN_SEPARATOR_VDC)<=0) {
			throw new IllegalArgumentException("FQN not well formed or does not contain an VDC");
		}
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        String organization = fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
        
		return getFQN(organization, customer);
	}
	
	public static String getService(String fqn) {
		if (fqn.indexOf(FQN_SEPARATOR_SERVICE)<=0) {
			throw new IllegalArgumentException("FQN not well formed or does not contain a service");
		}
		
        String service = fqn.substring(fqn.indexOf(FQN_SEPARATOR_SERVICE));
        String parts[] = service.split("\\.");
        service = parts[1];
        
        String rest = fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_SERVICE)-1);
        
		return rest + "." + FQN_SEPARATOR_SERVICE + "." + service;
	}
	
	public static String getVEE(String fqn) {
		if (fqn.indexOf(FQN_SEPARATOR_VEE)<=0) {
			throw new IllegalArgumentException("FQN not well formed or does not contain a vee");
		}
		
        String vee = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VEE));
        String parts[] = vee.split("\\.");
        vee = parts[1];
        
        String rest = fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VEE)-1);
        
		return rest + "." + FQN_SEPARATOR_VEE + "." + vee;
	}

	public static String getReplica(String fqn) {
		if (fqn.indexOf(FQN_SEPARATOR_REPLICA)<=0) {
			throw new IllegalArgumentException("FQN not well formed or does not contain a replica");
		}
		
		String replica = fqn.substring(fqn.indexOf(URICreation.FQN_SEPARATOR_REPLICA)+URICreation.FQN_SEPARATOR_REPLICA.length() + 1);
		int indexDot = replica.indexOf(".");
		if (indexDot > 0) {
			replica = replica.substring(0, indexDot);
		}
		
		return replica;
	}
	
	public static String getURIOrg(String fqn) {
		
		if (fqn.indexOf(FQN_SEPARATOR_VDC)>0) {
		
			String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
			
			return URI_ORG.replace("{org-id}", org.replace(".", "_"));
		} else
			return URI_ORG.replace("{org-id}", fqn.replace(".", "_"));
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
	
	public static String getURISnapshot(String fqnSnapshot) {
		
		String uriService = getURIVapp(fqnSnapshot);
        
        String veeReplica = fqnSnapshot.substring(fqnSnapshot.indexOf(FQN_SEPARATOR_REPLICA));
        String[] parts = veeReplica.split("\\.");
        veeReplica = parts[1];
		
        String snapshot = fqnSnapshot.substring(fqnSnapshot.indexOf(FQN_SEPARATOR_SNAPSHOT));
        String[] partsSnapshot = snapshot.split("\\.");
        snapshot = partsSnapshot[1];
        
		return uriService + "/" + veeReplica + "/snapshot/" + snapshot; 
	}
	
	public static String getURIHwItem(String fqnReplica) {
		String uriReplica = getURIVEEReplica(fqnReplica);
		
        String hwItem = fqnReplica.substring(fqnReplica.indexOf(FQN_SEPARATOR_NET));
        String[]  parts = hwItem.split("\\.");
        hwItem = FQN_SEPARATOR_NET + "_" + parts[1];
        
		return (uriReplica + URI_HW).replaceAll("\\{hwitem-id\\}", hwItem); 
	}
	
	public static String getURIVirtualHardwareItem(String fqnHardwareItem) {
		String uriReplica = getURIVEEReplica(fqnHardwareItem);

        String hwItem = fqnHardwareItem.substring(fqnHardwareItem.indexOf(FQN_SEPARATOR_HW));
        String[]  parts = hwItem.split("\\.");
        hwItem = parts[1];

		return (uriReplica + URI_HW).replaceAll("\\{hwitem-id\\}", hwItem); 
	}
	
	public static String getURINet(String fqn) {
		
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
        String net = fqn.substring(fqn.indexOf(FQN_SEPARATOR_NET));
        parts = net.split("\\.");
        net = parts[1];
        
		return URI_NET.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer).replace("{net-id}", net);
	}
	
	public static String getURINetAdd(String fqn) {
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
		return URI_NET_ADD.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer);
	}
	
	public static String getURIServiceAdd(String fqn) {
		String org= fqn.substring(0, fqn.indexOf(FQN_SEPARATOR_VDC)-1);
		
        String customer = fqn.substring(fqn.indexOf(FQN_SEPARATOR_VDC));
        String parts[] = customer.split("\\.");
        customer = parts[1];
        
		return URI_VAPP_ADD.replace("{org-id}", org.replace(".", "_")).replace("{vdc-id}", customer);
	}

	public static String getReplicaFromHardware(String hardwareFqn) {
		if (hardwareFqn.matches("[^.]+\\." + FQN_SEPARATOR_VDC + "\\.[^.]+\\." + FQN_SEPARATOR_SERVICE +
				"\\.[^.]+\\." + FQN_SEPARATOR_VEE + "\\.[^.]+\\." + FQN_SEPARATOR_REPLICA + "\\.[^.]+\\." +
				FQN_SEPARATOR_HW + "\\.[^.]+")) {
			// If the hardware FQN is well formed, return the substring before the ".hw"
			return hardwareFqn.split("\\." + FQN_SEPARATOR_HW)[0];
		} else {
			throw new IllegalArgumentException("The hardware FQN input was malformed");
		}
	}

	public static String getVeeFqn(String fqn) {
		if (fqn.matches("[^.]+\\." + FQN_SEPARATOR_VDC + "\\.[^.]+\\." + FQN_SEPARATOR_SERVICE +
				"\\.[^.]+\\." + FQN_SEPARATOR_VEE + "\\.[^.]+\\." + FQN_SEPARATOR_REPLICA + "\\..*")) {
			// If the FQN is well formed, return the substring before the ".replicas"
			return fqn.split("\\." + FQN_SEPARATOR_REPLICA)[0];
		} else {
			throw new IllegalArgumentException("The VEE FQN input was malformed");
		}
	}

	public static String getFQNFromURL(String url) {
		String fqn = null;
		if (url.indexOf(URI_API)>0){
			url = url.substring(url.indexOf(URI_API)+URI_API.length()+1);
		}
		if (url.indexOf("?")>0){
			url = url.substring(0,url.indexOf("?"));
		}		
		
		String[] elemens = url.split("/");
		String vm;
		String vee;
		String vapp;
		String vdc;
		String org;
	
		if (elemens.length ==2){
			org = elemens[1];
			fqn = getFQN(org);
		}
		else if (elemens.length ==4){
			org = elemens[1];
			vdc = elemens[3];
			fqn = getFQN(org, vdc);
		}	
		else if (elemens.length ==6){
			org = elemens[1];
			vdc = elemens[3];			
			vapp = elemens[5];
			fqn = getFQN(org, vdc, vapp);
		}		
		else if (elemens.length ==7){
			org = elemens[1];
			vdc = elemens[3];			
			vapp = elemens[5];			
			vee = elemens[6];
			fqn = getFQN(org, vdc, vapp, vee);
		}
		else if (elemens.length >=8){
			org = elemens[1];
			vdc = elemens[3];			
			vapp = elemens[5];			
			vee = elemens[6];			
			vm = elemens[7];
			fqn = getFQN(org, vdc, vapp, vee, vm);
		}
		return fqn;
	}	
}
