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
package com.telefonica.claudia.smi.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.telefonica.claudia.smi.DataTypesUtils;
import com.telefonica.claudia.smi.TCloudConstants;

public class TaskManager {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.task.TaskManager");
	
	Map<Long, Task> deployedTasks = new HashMap<Long, Task>();
	Map<String, ArrayList<Long>> userTasks  = new HashMap<String, ArrayList<Long>> ();
	
	String uri;
	
	private static TaskManager instance;
	
	private TaskManager(String uri) {
		this.uri = uri;
	}
	
	public static TaskManager createManager(String uri) {
		instance = new TaskManager(uri);
		
		return instance;
	}
	
	public static TaskManager getInstance() {
		if (instance==null) {
			log.error("Trying to retrieve a task manager before initializing one.");
			throw new IllegalStateException("Trying to retrieve a task manager before initializing one.");
		}
		
		return instance;
	}
	
	public Task addTask(Task t, String fqnUser) {
		
		synchronized(deployedTasks) {
			deployedTasks.put(t.getTaskId(), t);
		}
		
		synchronized(userTasks) {
			if (!userTasks.containsKey(fqnUser))
				userTasks.put(fqnUser, new ArrayList<Long>());
			
			userTasks.get(fqnUser).add(t.getTaskId());
		}
		
		t.setUriParent(uri);
		t.start();
		
		return t;
	}
	
	public void removeTask(long id) {
		
		synchronized(deployedTasks) {
			deployedTasks.remove(id);
		}
		
		String[] users;
		
		synchronized(userTasks) {
			users = userTasks.keySet().toArray(new String[userTasks.keySet().size()]);
		}
		
		synchronized(userTasks) {
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
}
