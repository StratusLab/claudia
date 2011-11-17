package com.telefonica.claudia.smi.provisioning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import com.telefonica.claudia.smi.provisioning.ONEProvisioningDriver.ControlActionType;
import com.telefonica.claudia.smi.task.Task.TaskError;

public class OneOperations {
	// XMLRPC commands to access OpenNebula features
	private final static String VM_ALLOCATION_COMMAND = "one.vm.allocate";
	private final static String VM_UPDATE_COMMAND = "one.vm.action";
	private final static String VM_GETINFO_COMMAND = "one.vm.info";
	private final static String VM_GETALL_COMMAND = "one.vmpool.info";
	private final static String VM_DELETE_COMMAND = "one.vm.delete";

	private final static String NET_ALLOCATION_COMMAND = "one.vn.allocate";
	private final static String NET_GETINFO_COMMAND = "one.vn.info";
	private final static String NET_GETALL_COMMAND = "one.vnpool.info";
	private final static String NET_DELETE_COMMAND = "one.vn.delete";

	String oneSession;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("com.telefonica.claudia.smi.provisioning.OneOperations");
	private XmlRpcClient xmlRpcClient = null;
	 private OneVmUtilities vmutils = null;
	
	public OneOperations (String oneSession, XmlRpcClient xmlRpcClient)
	{
		this.oneSession = oneSession;
		this.xmlRpcClient = xmlRpcClient;
		
	
	}
	
	public void configOperations (String oneversion, String networkBridge,
			String environmentRepositoryPath ,String oneScriptPath, String  oneSshKey ,String  customizationPort, String hypervisorInitrd, String hypervisorKernel,
			String xendisk, String arch, String server, String netInitScript0, String netInitScript1)
	{
		vmutils = new OneVmUtilities (this, oneversion, networkBridge,
				environmentRepositoryPath , oneScriptPath, oneSshKey , customizationPort, hypervisorInitrd, hypervisorKernel,
				xendisk, arch, server, netInitScript0, netInitScript1);
	}
	
	public String getOneNetworkListInfo () throws IOException
	{
		List rpcParams = new ArrayList ();
		rpcParams.add(oneSession);
		rpcParams.add(-2);
		rpcParams.add(-1);
		rpcParams.add(-1);

		log.debug("Get info about all networks: " );

		Object[] result = null;
		try {
			result = (Object[])xmlRpcClient.execute("one.vnpool.info", rpcParams);
		} catch (XmlRpcException ex) {
			log.error("Connection error trying to get network list information: " + ex.getMessage());
			throw new IOException ("Error on reading VM state , XMLRPC call failed", ex);
		}

		boolean completed = (Boolean) result[0];

		if (completed) {

			String resultList = (String) result[1];
			
			return resultList;
	    }
		else 
			return null;
	}
	
	public String deployVirtualMachine (String ovf, String fqnVm ) throws Exception
	{
		
		List<String> rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		String template = vmutils.TCloud2ONEVM(ovf, fqnVm);
		System.out.println ("VIRTUAL MACHINE ONE TEMPLATE \n"+ template);
		try {
			rpcParams.add(template);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object[] result = null;
		try {
			result = (Object[])xmlRpcClient.execute(VM_ALLOCATION_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			throw new IOException ("Error on allocation of VEE replica , XMLRPC call failed: " + ex.getMessage(), ex);
		}

		boolean success = (Boolean)result[0];

		if(success) {
			log.debug("Request succeded. Returining: \n\n" + ((Integer)result[1]).toString() + "\n\n");
			
			return ((Integer)result[1]).toString();
		} else {
			log.error("Error recieved from ONE: " + (String)result[1]);
			System.out.println("Error recieved from ONE: " + (String)result[1]);
			throw new Exception ((String)result[1]);
			
		}
	}
	
	public String deployNetwork(String xml) throws Exception {

		System.out.println ("NETWORK TEMPLATE\n" + xml);
		List<String> rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		
		// TCloud2ONENet(ovf)
		rpcParams.add(xml);
		Object[] result = null;
		try {
			result = (Object[])xmlRpcClient.execute(NET_ALLOCATION_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			log.error("Connection error. Could not reach ONE host: " + ex.getMessage());
			throw new IOException ("Error on allocation of the new network , XMLRPC call failed", ex);
		}

		boolean success = (Boolean)result[0];

		
		
		if(success) {
			log.debug("Network creation request succeded: \n\n" + ((Integer)result[1]).toString() + "\n\n");
			
			return ((Integer)result[1]).toString();
		} else {
			log.error("Error recieved from ONE: " + (String)result[1]);
			System.out.println("Error recieved from ONE: " + (String)result[1]);
			throw new Exception ((String)result[1]);
			
		}
	
     }
	

	public boolean deleteVirtualMachine(String id) throws IOException {
		
		System.out.println ("Deleting vm " + id);
		List rpcParams = new ArrayList ();

		ControlActionType controlAction = ControlActionType.finalize;
	
		rpcParams.add(oneSession);
		rpcParams.add(controlAction.toString());
		rpcParams.add(Integer.parseInt(id));

		Object[] result = null;

		try {
			result = (Object[])xmlRpcClient.execute(VM_UPDATE_COMMAND, rpcParams);

		} catch (XmlRpcException ex) {
			log.error("Connection error trying to update VM: " + ex.getMessage());
			throw new IOException ("Error updating VEE replica , XMLRPC call failed", ex);
		}

		if (result==null) {
			throw new IOException("No result returned from XMLRPC call");
		} else {
			return (Boolean)result[0];
		}
	}
	
	public boolean deleteNetwork(String id) throws IOException {

		List rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		rpcParams.add(new Integer(id) );
		Object[] result = null;
		try {
			result = (Object[])xmlRpcClient.execute(NET_DELETE_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			throw new IOException ("Error deleting the network , XMLRPC call failed", ex);
		}

		boolean success = (Boolean)result[0];

		if(success) {
			return (Boolean)result[0];
		} else {
			throw new IOException("Unknown error trying to delete network: " + (String)result[1]);
		}
	}

	
	public String getOneNetworkId (String fqn) throws IOException
	{
		String info = getOneNetworkListInfo ();
		ONEUtilities util = new ONEUtilities ();
		String id = util.obtainIdOneNetworkFromFQN(fqn, info);
	
		
		System.out.println ("Id for fqn " + fqn + " : " + id );
		return id;
		
	}
	
	public String getVirtualMachine ( String id ) throws Exception
	{
		
		List rpcParams = new ArrayList<String>();
		rpcParams.add(oneSession);
		rpcParams.add(new Integer(id) );
	
		Object[] result = null;
		try {
			result = (Object[])xmlRpcClient.execute(VM_GETINFO_COMMAND, rpcParams);
		} catch (XmlRpcException ex) {
			throw new IOException ("Error on allocation of VEE replica , XMLRPC call failed: " + ex.getMessage(), ex);
		}

		boolean completed = (Boolean) result[0];

		if (completed) {

			String resultList = (String) result[1];

			return resultList;
	    }
		else 
			return null;
	}
	
	public boolean doAction (String id, String action) throws IOException
	{
		List rpcParams = new ArrayList ();
		ControlActionType controlAction  = null;
		
		log.info("Id for action " + id + " action " + action);

	
		if (action.equals("powerOn"))
		{
		   controlAction = ControlActionType.resume;
		}
		else if (action.equals("powerOff"))
	    {
			controlAction = ControlActionType.stop;
	    }
		
		rpcParams.add(oneSession);
		rpcParams.add(controlAction.toString());
		rpcParams.add(Integer.parseInt(id));
		
		log.info("Sending call for power..");
		Object[] result = null;
		
		try {				
			result = (Object[])xmlRpcClient.execute(VM_UPDATE_COMMAND, rpcParams);
			
		} catch (XmlRpcException ex) {
			log.error("Connection error trying to update VM: " + ex.getMessage());
			throw new IOException ("Error updating VEE replica , XMLRPC call failed", ex);
		}
		
		if (result==null) {
			log.error("No result returned from XMLRPC call");
			throw new IOException("No result returned from XMLRPC call");
		} else {
			return (Boolean)result[0];
		}
	}
	
}
	
	
