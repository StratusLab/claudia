/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;
import com.telefonica.claudia.smi.URICreation;

public class ClaudiaTaskManager extends TaskManager{
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.task.ClaudiaTaskManager");
	
	Map<Long, Task> deployedTasks = new HashMap<Long, Task>();
	Map<String, ArrayList<Long>> userTasks  = new HashMap<String, ArrayList<Long>> ();
	
	String uri;
	
	private Long actualTaskId  =1l;
	private Long sequence  =1l;
	
	private synchronized long generateNewId() {
		long timeStamp = System.currentTimeMillis();
		if (sequence>1000){
			sequence = 1L;
		}
		return ((timeStamp*1000)+sequence);
	}
	
	public ClaudiaTaskManager(Properties prop) {
		String serverHost = prop.getProperty(KEY_HOST);
		log.info("serverHost read: " + serverHost);
		
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(prop.getProperty(KEY_PORT));
			log.info("serverPort read: " + serverPort);
		} catch (NumberFormatException nfe) {
			log.error("Error parsing the " + KEY_PORT
					+ " property. A number was expected, but '"
					+ prop.getProperty(KEY_PORT) + "' was found.");
			System.exit(ERROR_CODE_INITIALIZATION);
		}
		if ( prop.getProperty(KEY_ORGANIZATION) ==null){
			log.error("Error parsing. The " + KEY_ORGANIZATION
					+ " property was not found.");
			System.exit(ERROR_CODE_INITIALIZATION);
		}
	    uri = PROTOCOL + serverHost + ":" + serverPort + URICreation.URI_ORG.replace("{org-id}", prop.getProperty(KEY_ORGANIZATION)).replace(".", "_") + "/task";

	    //CacheFactory factory = new DefaultCacheFactory();

	    //pcache = factory.createCache(JCACHE_CONFIG_FILE, true);
	    //pcache.start();

	}	
	
	public TaskManager createManager(TaskManager taskManager) {
		TaskManager.instance = taskManager;		
		return TaskManager.instance;
	}
	
	public Task addTask(Task t, String fqnUser) {
		
		t.setTaskId(generateNewId());
		
		synchronized(deployedTasks) {
			deployedTasks.put(t.getTaskId(), t);
		}
		
		synchronized(userTasks) {
			if (!userTasks.containsKey(fqnUser))
				userTasks.put(fqnUser, new ArrayList<Long>());
			
			userTasks.get(fqnUser).add(t.getTaskId());
		}
		
		t.setUriParent(uri);
		
		//Fqn newTask = Fqn.fromString("/pojo/" + t.getTaskId());

		//@SuppressWarnings("unused")
		//Object content = pcache.put(newTask, "taskDescriptor", t);
		
		t.start();
		
		return t;
	}
	
	public void removeTask(long id) {
		
		synchronized(deployedTasks) {
			deployedTasks.remove(id);
		}
		
		synchronized(userTasks) {
			String[] users = userTasks.keySet().toArray(new String[userTasks.keySet().size()]);
			for (String user: users) {
					userTasks.get(user).remove(id);
			}
		}
	}
	
	public Task getTask(long id) {
		
		Task returnValue;
		
		synchronized(deployedTasks) {
			returnValue = deployedTasks.get(id);
		}
		
		return returnValue;
	}
	
	public String taskList(String fqnUser) throws IOException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc;
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element root = doc.createElement(TCloudConstants.TAG_TASKS);
			root.setAttribute("href", uri);
			doc.appendChild(root);
			
			boolean contained=false;
			Long[] currentTasks;
			
			synchronized(userTasks) {
				contained = userTasks.containsKey(fqnUser);
			}
			
			if (contained) {
				
				synchronized(userTasks) {
					currentTasks = userTasks.get(fqnUser).toArray(new Long[userTasks.get(fqnUser).size()]);
				}
				
				for (Long taskId: currentTasks) { 
					
					Task currentTask = deployedTasks.get(taskId);
					
					Node description;
					
					synchronized (currentTask) {
						description = currentTask.getXmlDescription().getFirstChild();
					}
					
					root.appendChild(doc.importNode(description, true));
				}
			}
			
		} catch (ParserConfigurationException e) {
			throw new IOException("Error writing the XML response: " + e.getMessage());
		}
	    
		return DataTypesUtils.serializeXML(doc);
	}
	
	public static final String PROTOCOL = "http://";	
	private static final String KEY_HOST = "com.telefonica.claudia.server.host";
	private static final String KEY_PORT = "com.telefonica.claudia.server.port";
	private static final String KEY_ORGANIZATION = "com.telefonica.claudia.organization";
	private static final int ERROR_CODE_INITIALIZATION = 1;	
}
