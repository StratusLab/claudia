package com.telefonica.claudia.slm.recovery;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
public class Subject implements PersistentObject {
	
	@Id
	@GeneratedValue
	protected long internalId;
	
	/**
	 * Internal BBDD id of the subject of the operation.
	 */
	private Long id;
	
	/**
	 * Fully qualified class name of the subject.
	 */
	private String className;
	
	/**
	 * FQN of the subject of the operation.
	 */
	private String fqn;
	
	
	private String taskId;
	
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassName() {
		return className;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setFqn(String fqn) {
		this.fqn = fqn;
	}
	public String getFqn() {
		return fqn;
	}
	public void setTaskId(String taksId) {
		this.taskId = taksId;
	}
	public String getTaskId() {
		return taskId;
	}
	public long getObjectId() {
		return internalId;
	}
	
}
