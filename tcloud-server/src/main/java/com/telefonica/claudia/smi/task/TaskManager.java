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
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefonica.claudia.smi.provisioning.ProvisioningDriver;

public abstract class TaskManager {
	
	private static Logger log = Logger.getLogger("com.telefonica.claudia.smi.task.TaskManager");
	
	protected static TaskManager instance;
	
	public abstract TaskManager createManager(TaskManager taskManager);
	
	public static TaskManager getInstance() {
		if (instance==null) {
			log.error("Trying to retrieve a task manager before initializing one.");
			throw new IllegalStateException("Trying to retrieve a task manager before initializing one.");
		}
		
		return instance;
	}
	
	public abstract Task addTask(Task t, String fqnUser);
	
	public abstract void removeTask(long id);
	
	public abstract Task getTask(long id);
	
	public abstract String taskList(String fqnUser) throws IOException;
}
