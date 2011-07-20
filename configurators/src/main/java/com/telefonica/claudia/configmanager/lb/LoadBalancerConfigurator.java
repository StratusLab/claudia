package com.telefonica.claudia.configmanager.lb;

import java.util.List;
import java.util.Map;

import com.telefonica.claudia.configmanager.Configurator;

/**
 * This interface is used to configure the Load Balancer in order to add/remove
 * nodes.
 * Requires the com.telefonica.claudia.configmanager.common package to show descriptives
 * return values.
 * 
 * @author amartin
 * 
 */
public interface LoadBalancerConfigurator extends Configurator {

	/**
	 * Adds a node to given Load Balancer
	 * 
	 * @param ipLb
	 *            Load Balancer IP
	 * @param portLb
	 *            Load Balancer port
	 * @param fqnNode
	 *            Node's Full Qualified Name (FQN)
	 * @param ipNode
	 *            Node's IP
	 * @return SUCCESS_NODE_ADD if the node was successfully added to the Load Balancer.
	 *		   ERROR_EXISTING_FQN if the node already exists in the Load Balancer.
	 *		   ERROR_INTERNAL_SERVER_ERROR if the operation couldn't be completed due to
	 *				an internal server error.
	 */
	public int addNode(String ipLb, int portLb, String fqnNode, String ipNode);

	/**
	 * Adds a list of nodes to given Load Balancer. This operation executes atomically.
	 * If one node can't be added (because already exists in the Load Balancer) the entire operation fails.
	 * 
	 * @param ipLb
	 *            Load Balancer IP
	 * @param portLb
	 *            Load Balancer port
	 * @param nodes
	 *            Map containing <b>Full Qualified Name</b> and <b>IP</b> pairs
	 * @return SUCCESS_NODE_ADD if all the given nodes were successfully added to the Load Balancer.
	 *		   ERROR_EXISTING_FQN if any node in the FQN list exists in the Load Balancer.
	 *		   ERROR_INTERNAL_SERVER_ERROR if the operation couldn't be completed due to
	 *				an internal server error.
	 */
	public int addNodes(String ipLb, int portLb, Map<String, String> nodes);

	/**
	 * Updates node ip for given Load Balancer
	 * 
	 * @param ipLb
	 *            Load Balancer IP
	 * @param portLb
	 *            Load Balancer port
	 * @param fqnNode
	 *            Node Full Qualified Name
	 * @param ipNode
	 *            Node IP
	 * 
	 * @return SUCCESS_NODE_IP_UPDATE if the node's IP was successfully updated.
	 *		   ERROR_NOT_AVAILABLE_NODE if the node doesn't exist in the Load Balancer.
	 *		   ERROR_INTERNAL_SERVER_ERROR if the operation couldn't be completed due to
	 *				an internal server error.
	 */
	public int updateNode(String ipLb, int portLb, String fqnNode, String ipNode);

	/**
	 * Removes a node for given Load Balancer
	 * 
	 * @return SUCCESS_NODE_DELETE if the node was successfully removed from the
	 *         Load Balancer. ERROR_NOT_AVAILABLE_NODE if the node doesn't exist
	 *         in the Load Balancer. ERROR_INTERNAL_SERVER_ERROR if the
	 *         operation couldn't be completed due to an internal server error
	 */
	public int removeNode(String ipLb, int portLb, String fqnNode);

	/**
	 * Removes a list of nodes from given Load Balancer. This operation does not exectues atomically.
	 * If a node can't be deleted (because it doesn't exists in the Load Balancer), the operation
	 * tries to delete the rest of the given fqn's list.
	 * 
	 * @param ipLb
	 *            Load Balancer IP
	 * @param portLb
	 *            Load Balancer port
	 * @param nodeList
	 *            List of nodes
	 * @return a list that shows for each node if it was successfully deleted from the given load balancer or not.
	 */
	public List<String> removeNodes(String ipLb, int portLb, List<String> nodeList);

	/**
	 * Gets all nodes from given Load Balancer
	 * 
	 * @param ipLb
	 * @param portLb
	 * @return a list of existing nodes (fqn's) in the load balanacer
	 */
	public List<String> getNodes(String ipLb, int portLb);
	
	public String [] getNodeRemove(String ipLb, int portLb,  int numbernodes) throws Exception;

}
