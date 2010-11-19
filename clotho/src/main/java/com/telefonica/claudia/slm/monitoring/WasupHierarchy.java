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
package com.telefonica.claudia.slm.monitoring;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.telefonica.claudia.slm.deployment.Customer;
import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.ServiceKPI;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.CPU;
import com.telefonica.claudia.slm.deployment.hwItems.Disk;
import com.telefonica.claudia.slm.deployment.hwItems.Memory;
import com.telefonica.claudia.slm.deployment.hwItems.NIC;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;


public class WasupHierarchy {
	
	private static Logger log = Logger.getLogger("Monitoring");
	
	/**
	 * The Hierachy is implemented with a singleton pattern. This attribute
	 * holds the actual instance.
	 */
	private static WasupHierarchy actualHierarchy;
	
    /**
     * Map containing all the nodes indexed by its data FQN.
     */
	private HashMap<FQN, WasupNode> wasupIds= new HashMap<FQN, WasupNode> ();
	
	/**
	 * Root of the actual Wasup hierarchy.
	 */
	private WasupNode wasupRoot;
	
	/**
	 * Wasup Rest client to make the actual calls.
	 */
	private static WasupClient client;
	
	/**
	 * Element type in wasup. The element types are fixed for the Service Manager,
	 * but they need to be recreated each time a new SM is deployed.
	 *
	 */
	private static class WasupType {
		String id;
		String name;
		String description;
		String unit="";
		String[] range=new String[] {null, null};
		
		public WasupType(String name, String description) {
			this.name=name;
			this.description=description;
		}
		
		public WasupType(String name, String description, String unit, String lowerBound, String upperBound) {
			this(name, description);
			
			this.unit= unit;
			this.range[0] = lowerBound;
			this.range[1] = upperBound;
		}		
	}
	
    /**
     * Wasup element types. There should be one type per class in the deployment model. Its key
     * is the fully qualified class name of each class and the vale an array with two strings:
     * the first being the name of the element type and the second being the element description.
     */
    private static HashMap<String, WasupType> wasupTypes = new  HashMap<String, WasupType> ();

    /**
     * Wasup id of each measurement type hold by the hierarchy. Each measurment type wasup id (value)
     * is associated with an element FQN (key).
     */
    private HashMap<String, String> measureTypeIds = new HashMap<String, String> ();
    
    /**
     * Collection that maps class names of each element to the String identifier in wasup of its
     * measurement type.
     */
    private static HashMap<String, String> measurementTypeStrings = new HashMap<String, String>();
    
    static {
    	measurementTypeStrings.put(NIC.class.getName(), "Network Interface");
    	measurementTypeStrings.put(Disk.class.getName(), "Disk");
    	measurementTypeStrings.put(CPU.class.getName(), "CPU");
    	measurementTypeStrings.put(ServiceKPI.class.getName(), "ServiceKPI");
    	measurementTypeStrings.put(Memory.class.getName(), "Memory");
    }
    
    /**
     * Array containing the measure types that are pending of being created. During element creation,
     * some of the elements may have a measurement type associated. For those element, an entry in this
     * array is created, with its FQN. When the publishMeasureTypes() method is invoked, all of them are
     * created in wasup and the array is emptied.
     */
    private Set<DirectoryEntry> pendingMeasureTypes = new HashSet <DirectoryEntry>();

    private static String CUSTOMER_TYPE_NAME= null;
    private static String SERVICE_TYPE_NAME= null;
    private static String VEE_TYPE_NAME= null;
    private static String VEEREPLICA_TYPE_NAME= null;
    private static String NETWORK_TYPE_NAME= null;
    private static String HW_TYPE_NAME= null;
    
    public static void setParameters(String customer, String service, String vee, String veeReplica, String network, String hardware) {
    	CUSTOMER_TYPE_NAME= customer;
        SERVICE_TYPE_NAME= service;
        VEE_TYPE_NAME= vee;
        VEEREPLICA_TYPE_NAME= veeReplica;
        NETWORK_TYPE_NAME= network;
        HW_TYPE_NAME= hardware;

    	wasupTypes.put(Customer.class.getCanonicalName(), new WasupType(CUSTOMER_TYPE_NAME, "Customer"));
    	
    	WasupType serviceType = new WasupType(SERVICE_TYPE_NAME, "Service Application", " ", null, null);
    	wasupTypes.put(ServiceApplication.class.getCanonicalName(), serviceType);
    	wasupTypes.put(ServiceKPI.class.getCanonicalName(), serviceType);

    	wasupTypes.put(VEE.class.getCanonicalName(), new WasupType(VEE_TYPE_NAME, "Virtual Execution Environment"));
    	wasupTypes.put(VEEReplica.class.getCanonicalName(), new WasupType(VEEREPLICA_TYPE_NAME, "VEE Replica"));
    	wasupTypes.put(Network.class.getCanonicalName(), new WasupType(NETWORK_TYPE_NAME, "Network Interface", " ", null, null));
    	
    	WasupType hwType = new WasupType(HW_TYPE_NAME, "Hardware Item", " ", "0.0", null);
    	wasupTypes.put(CPU.class.getCanonicalName(), hwType);
    	wasupTypes.put(Disk.class.getCanonicalName(), hwType);
    	wasupTypes.put(Memory.class.getCanonicalName(), hwType);
    	wasupTypes.put(NIC.class.getCanonicalName(), hwType);
    }
    
    
    public static void loadWasupTypes() throws IOException, JSONException {
    	client = WasupClient.getWasupClient();
    	
    	for (WasupType type: wasupTypes.values()) {
    		int typeId=client.getElementTypeId(type.name);
    		
    		if (typeId==-1) {
    			client.createElementType(type.name, type.description);
    			log.debug("Type: " + type.name + " created in Wasup with id: " + type.id);
    		} else {
    			type.id = String.valueOf(typeId);
    			log.debug("Type: " + type.name + " found in Wasup with id: " + type.id);
    		}
    	}
    }
	
    /**
     * Node of the actual hierarchy. Each node holds a Wasup element representing an entity in the Reservoir 
     * directory. It also contains a reference to its Element type. A reference to the real Reservoir object 
     * is hold in the <i>data</i> field.
     *  
     * Each node contains a list of its descendant and a reference to its parent for navegability purposes.
     * 
     */
	private class WasupNode {
		DirectoryEntry data;
		WasupNode parent;
		WasupType type;
		
		String wasupId;
		String parentString;
		Set<WasupNode> children = new HashSet<WasupNode>();
		
		String[] range=new String[] {"0", "100"};
		
		public WasupNode(DirectoryEntry data) {
			this.data = data;
			
			WasupHierarchy.this.wasupIds.put(data.getFQN(), this);
			type = WasupHierarchy.wasupTypes.get(data.getClass().getCanonicalName());
			
			range[0] = type.range[0];
			range[1] = type.range[1];
		}
		
		public void add(WasupNode child) {
			children.add(child);
			child.parent = this;
		}
		
		public boolean equals(Object otherNode) {
			
			if (!(otherNode instanceof WasupNode)) 
				return false;
			
			return data.getFQN().equals( ((WasupNode)otherNode).data.getFQN() );
		}
		
	    public int hashCode() {
	        return data.getFQN().hashCode();
	    }
	}

	/**
	 * Returns the actual working instance of the hierarchy class, creating a new
	 * when necessary.
	 * 
	 * @return
	 * 		The actual working instance
	 * 
	 * @throws Exception
	 */
	public static WasupHierarchy getWasupHierarchy() throws Exception {
		if (actualHierarchy==null) {
			actualHierarchy = new WasupHierarchy();
		} 
		
		return actualHierarchy;
	}
	
	private WasupHierarchy() throws Exception {
		client = WasupClient.getWasupClient();
	}
	
	/**
	 * Creates the wasup element hierarchy based upon the information of the given
	 * ServiceApplication object. The hierarchy is built as a tree composed of WasupNodes.
	 * A HashMap of WasapNodes is also created to efficiently update the hierarchy if
	 * there are any changes in one point in the tree.
	 * 
	 * Once the tree is created in memory, the method make all the Rest Calls needed in order to
	 * create it in Wasup.
	 * 
	 * @param sp
	 * 		ServiceApplication object which is to be deployed on the Wasup
	 * 
	 * @throws IOException 
	 * @throws JSONException 
	 * 
	 */
	public void createWasupHierarchy(ServiceApplication sp) throws IOException, JSONException {
		
		log.info("Creating Wasup hierarchy for Service Application " + sp.getFQN().toString());
		
		// The customer will be the root of the hierarchy, followed by the service
		if (wasupRoot==null) {
			Customer customer = sp.getCustomer();
			
			wasupRoot = new WasupNode(customer);
		}
		
		WasupNode serviceNode = new WasupNode(sp);
		wasupRoot.add(serviceNode);
		
		// Down the service, all the VEES are created. Below each VEE, the VEEReplicasn are placed
		// and, under them, all the hardware items present in each VEE.
		for (VEE vee: sp.getVEEs()) {
			log.info("\tCreating node for VEE " + vee.getFQN().toString());
			addNode(vee, sp);
		}
		
		// Create the network nodes 
		for (Network net: sp.getNetworks()) {
			log.info("\tCreating node for Network " + net.getFQN().toString());
			addNode(net, sp);
		}
		
		// Create the KPIs also
		for (ServiceKPI kpi: sp.getServiceKPIs()) {
			log.info("\tCreating node for VEE " + kpi.getFQN().toString());
			pendingMeasureTypes.add(kpi);
			addNode(kpi, sp);
		}
		
		// Check if the Customer is already created
		int idCustomer;
		int customerType;
		
		try {
			customerType = Integer.parseInt(wasupTypes.get(wasupRoot.data.getClass().getCanonicalName()).id);
		} catch (NumberFormatException nfe) {
			log.error("WASUP hierarchy could not be created: wrong customer type ID");
			return;
		}
		
		if ((idCustomer = client.getElementId(wasupRoot.data.getFQN().toString(), customerType))!= WasupClient.NO_ELEMENT_FOUND) {
			wasupRoot.wasupId = String.valueOf(idCustomer);
			publishNode(serviceNode);
		} else {
			// The hierarchy is now created, send it to Wasup
			publishNode(wasupRoot);
		}
		
		publishMeasureTypes();
	}
	
	/**
	 * Make all the Rest calls needed to create the actual node and all of its descendants in WASUP. 
	 * 
	 * @param node
	 * 		Node to publis in the wasup.
	 * 
	 * @param parentString
	 * 		Path between the root of the wasup Rest server and the node to be deloyed.
	 * @throws IOException 
	 * @throws JSONException 
	 */
	private void publishNode(WasupNode node) throws IOException, JSONException {
		
		// Create the actual node
		if (node.parent==null)
			node.parentString="";
		else
			node.parentString = node.parent.parentString + "/" + node.parent.wasupId;
		
		// Make the rest call
		node.wasupId = String.valueOf(client.createElement((node.parent!=null)?node.parent.wasupId:"", 
														   node.data.getFQN().toString(), 
														   wasupTypes.get(node.data.getClass().getCanonicalName()).id));
		
		// Publish each descendant of the actual node
		for (WasupNode child: node.children) 
			publishNode(child);
	}
	
	/**
	 * Remove the directory entry passed as a parameter from the local hierarchy and from the 
	 * wasup.
	 *  
	 * @param node
	 * 		Directory entry to be removed.
	 * 
	 * @throws IOException 
	 */
	public void removeNode(DirectoryEntry node) throws IOException {
		
		// First of all, remove the node from  the hierarchy
		WasupNode nodeToRemove = wasupIds.get(node.getFQN());
		
		removeNode(nodeToRemove);
		
		if (nodeToRemove.parent!= null) {
			nodeToRemove.parent.children.remove(nodeToRemove);
		}
	}
	
	private void removeNode(WasupNode nodeToRemove) throws IOException {
		
		if (nodeToRemove == null) {
			log.error("Null object realeasing the hierarchy");
			return;
		}
		
		// First of all, remove all its children
		synchronized(nodeToRemove.children) {
			Iterator<WasupNode> iter = nodeToRemove.children.iterator();
			
			while (iter.hasNext()) {
				removeNode(iter.next());
				iter.remove();
			}
		}
		
		wasupIds.remove(nodeToRemove.data.getFQN());
		
		// Call the wasup to actually delete the element.
		client.removeElement(nodeToRemove.wasupId);
	}
	
	/**
	 * Add the directory entry node to the wasup hierarchy under the node of the
	 * directory entry parent, and update the wasup hierarchy accordingly.
	 * 
	 * @param parent
	 * 		Parent directory entry under which the new node will be added.
	 * 
	 * @param node
	 * 		Directory entry to be added.
	 * @throws IOException 
	 * @throws JSONException 
	 */
	private WasupNode addNode(DirectoryEntry data, DirectoryEntry parent)  throws IOException, JSONException {
		
		WasupNode parentNode = wasupIds.get(parent.getFQN());
		WasupNode node = new WasupNode(data);
		parentNode.add(node);
		
		return node;
	}
	
	private WasupNode addNode(VEE vee, DirectoryEntry parent) throws IOException, JSONException {
		
		WasupNode serviceNode = wasupIds.get(parent.getFQN());
		WasupNode veeNode = new WasupNode(vee);
		serviceNode.add(veeNode);
		
		for (VEEReplica replica: vee.getVEEReplicas()) {
			addNode(replica, vee);
		}
		
		return veeNode;
	}
	
	private WasupNode addNode(VEEReplica replica, DirectoryEntry parent) throws IOException, JSONException {
		
		log.debug("Adding replica [" + replica.getFQN() + "] to the Wasup hierarchy");
		
		WasupNode veeNode = wasupIds.get(parent.getFQN());
		WasupNode veeReplicaNode = new WasupNode(replica);
		veeNode.add(veeReplicaNode);
		
		for (NIC nic: replica.getNICs()) {
			WasupNode nicNode = new WasupNode(nic);
			veeReplicaNode.add(nicNode);
			
			pendingMeasureTypes.add(nic);
		}
		
		for (CPU cpu: replica.getCPUs()) {
			WasupNode cpuNode = new WasupNode(cpu);
			veeReplicaNode.add(cpuNode);
			
			pendingMeasureTypes.add(cpu);
		}

		for (Disk disk: replica.getDisks()) {
			WasupNode diskNode = new WasupNode(disk);
			veeReplicaNode.add(diskNode);
			
			diskNode.range[1] = String.valueOf(disk.getDiskConf().getCapacity());
			
			pendingMeasureTypes.add(disk);
		}
		
		WasupNode memoryNode = new WasupNode(replica.getMemory());
		veeReplicaNode.add(memoryNode);
		
		memoryNode.range[1] = String.valueOf(replica.getMemory().getMemoryConf().getCapacity());
		
		pendingMeasureTypes.add(replica.getMemory());
		
		log.debug("Replica added");
		
		return veeReplicaNode;
	}
	
	public void createVEE(VEE vee, DirectoryEntry parent) throws IOException, JSONException {
		publishNode(addNode(vee, parent));
	}
	
	public void createVEEReplica(VEEReplica replica, DirectoryEntry parent) throws IOException, JSONException {
		publishNode(addNode(replica, parent));
		
		publishMeasureTypes();
	}
	
	/**
	 * Publish the pending measure types to the Wasup. Measure types may be created during element creation 
	 * (no every element is associated to a measure type). Measure types to be created are stored in the
	 * pendingMeasureTypes array.
	 * @throws JSONException 
	 * @throws IOException 
	 * 
	 */
	public void publishMeasureTypes() throws IOException, JSONException {
		for (DirectoryEntry node: pendingMeasureTypes) {
			createMeasureType(node);
		}
		
		pendingMeasureTypes.clear();
	}
	
	/**
	 * Create a measure typa in the wasup, associated to the given element, and store its wasup id.
	 * 
	 * @param element
	 * 		Fully qualified name of the element associated to the measure.
	 * @throws JSONException 
	 * @throws IOException 
	 */
    private void createMeasureType(DirectoryEntry element) throws IOException, JSONException {
        
        WasupNode node = wasupIds.get(element.getFQN());

        log.info("Create a measurement type for element " + element.getFQN().toString());

        String name=element.getFQN().toString();
       
        if (name.contains("replicas")) {
            name=name.substring(name.indexOf("replicas"));
            name=name.substring(name.indexOf(".", name.indexOf("."))+1);
            name=name.substring(name.indexOf(".")+1);
            if (name==null || name.equals(""))
                name=element.getFQN().toString();
        } else if (name.contains("kpis")) {
            name=name.substring(name.indexOf("kpis"));
        }
       
        String itemName = element.getFQN().toString();
       
        measureTypeIds.put(element.getFQN().toString(), String.valueOf(client.createMeasurementType(name,
                                                                         itemName,
                                                                         node.parent.wasupId,
                                                                         node.type.unit,
                                                                         measurementTypeStrings.get(element.getClass().getName()), node.range[0],  node.range[1])));
       
       
       
        log.info("Creating measurement range for measure type " + measureTypeIds.get(element.getFQN().toString()) + " and values in [" +
                node.range[0] +"," + node.range[1] + "]");
        client.createMeasurementRange(measureTypeIds.get(element.getFQN().toString()), node.range[0], node.range[1]);
    }

	
	/**
	 * Post a measure value to the indicated measure type. The measure type must be checked in the 
	 * measureTypesList in order to get the actual Wasup id to make the real call.
	 * 
	 * @param measureType
	 * 		Fully qualified name of the measure type.
	 * 
	 * @param value
	 * 		String representation of the measure value.
	 * @throws JSONException 
	 * @throws IOException 
	 * 
	 */
	public void postMeasure(DirectoryEntry measureType, String value) throws IOException, JSONException {
		if (measureType==null) {
			log.error("A null measureType has been found.");
			return;
		}
		
		log.info("Measure[" + measureType.getFQN() + "]: " + value);
		client.postMeasure(measureType.getFQN().toString(), value);
	}
}
