/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.w3c.dom.Document;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.monitoring.MeasurableElement.MeasureDescriptor;
import com.telefonica.claudia.slm.recovery.OperationLog;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class CloudProvider implements PersistentObject {

	private static final String UNSUPPORTED_OPERATION = "This operation is currently not supported, please, use a subclass"; 
	
	@Id
	@GeneratedValue
	public long internalId;
	protected String host;
	protected int port;
	
	public enum ProvidedAction {DEPLOY, UNDEPLOY, POWER, GET_TASK};
	
	public static class ActionResult {
		private String message;
		private boolean finished;
		private boolean successful;
		
		public void setSuccessful(boolean successful) {
			this.successful = successful;
		}
		public synchronized boolean isSuccessful() {
			return successful;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public synchronized String getMessage() {
			return message;
		}
		public void setFinished(boolean finished) {
			this.finished = finished;
		}
		public synchronized boolean isFinished() {
			return finished;
		}
	}
	@Transient
	protected Thread internalThread = new Thread() {
		public void run() {
			
			while(true) {
				Runnable task=null;
				
				try {
					task = taskQueue.take();
				} catch (InterruptedException e) {
					
				}
				
				if (task!= null)
					task.run();
			}
		}
	};
	
	@Transient
	protected Map<VEEReplica, Map<ProvidedAction, ActionResult>> actionResults = new HashMap<VEEReplica, Map<ProvidedAction, ActionResult>>();
	
	@Transient
	protected BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
	
	@OneToMany(cascade=CascadeType.ALL)
	protected Map<String, Resource> resourceList=new HashMap<String, Resource>(); 
	
	@OneToMany(mappedBy="deployedOn", fetch=FetchType.EAGER)
	protected List<VEEReplica> allocatedVEEReplicas = new ArrayList<VEEReplica>();
	
	@Transient
	protected boolean providerAvailable=false;
	
	public CloudProvider() {
		internalThread.start();
	}
	
	public String getName() {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public String getImageFormat() {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public String getLocationCode() {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}
	
	public Collection<Resource> getResourceList() {
		return resourceList.values();
	}
	
	public void addResource(String type, Resource res) {
		resourceList.put(type, res);
	}
	
	public void deployReplica(VEEReplica rep) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public void undeployReplica(VEEReplica rep, OperationLog ol) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}
	
	public void removeReplica(VEEReplica rep) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public String powerReplica(VEEReplica veeReplicaToPower, String action, OperationLog ol) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public String[] getTaskStatus(String taskIdentifier) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	
	public String getMeasure(VEEReplica veeReplica, Map<String, String> measureValues, String measureName,
						String measureFilterFrom, String measureFilterTo, String measureFilterNumber, String measureFilterInterval) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}


	public String getMeasure(NIC nic, Map<String, String> measureValues, String measureName, String measureFilterFrom,
						String measureFilterTo, String measureFilterNumber, String measureFilterInterval) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}


	public String getMeasure(String fqn, String url, Map<String, String> measureValues, String measureName,
						String measureFilterFrom, String measureFilterTo, String measureFilterNumber, String measureFilterInterval) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}


	
	public MeasureDescriptor[] getMeasureDescriptorList(VEEReplica vr) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public MeasureDescriptor[] getMeasureDescriptorList(NIC vr) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	public MeasureDescriptor[] getMeasureDescriptorList(String fqn, String url) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}

	
	/**
	 * Returns the information available in the cloud provider for the given replica.
	 * 
	 * @param vr
	 * @return
	 */
	public Document getRemoteReplicaDescription(VEEReplica vr) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
	}
	
	public ActionResult getResult(VEEReplica rep, ProvidedAction action) {
		synchronized(actionResults) {
			if (actionResults.get(rep)==null) return null;
			
			return actionResults.get(rep).get(action);
		}
	}
	
	public void removeResult(VEEReplica rep, ProvidedAction action) {
		synchronized (actionResults) {
			if (actionResults.get(rep)==null) return;
			else if (actionResults.get(rep).get(action)==null) return;
			else actionResults.get(rep).remove(action);
		}
		
	}

	public void setProviderAvailable(boolean providerAvailable) {
		this.providerAvailable = providerAvailable;
	}

	public boolean isProviderAvailable() {
		return providerAvailable;
	}

	public long getObjectId() {
		return internalId;
	}
	
    @Override
    public int hashCode() {
        return (int) getName().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        
        if(object == null)
            return false;
        
        if(!(object instanceof CloudProvider))
            return false;
        
        return (((CloudProvider)object).getName().equals(this.getName()));
    }

	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
}
