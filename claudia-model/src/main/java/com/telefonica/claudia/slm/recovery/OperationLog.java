package com.telefonica.claudia.slm.recovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.naming.DirectoryEntry;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class OperationLog implements PersistentObject {
	
	@Id
	@GeneratedValue
	protected long internalId;
	
	/**
	 * Identifier of the operation type.
	 */
	protected String opType;

	/**
	 * List of subjects of the operation.
	 */
	@OneToMany(fetch=FetchType.EAGER)
	protected Set<Subject> subjects = new HashSet<Subject>();
	
	/**
	 * Current status of the operation. Subclasses may add new types of status.
	 */
	protected String status;
	public final static String STATE_INIT="init";
	public final static String STATE_FAILED="failed";
	public final static String STATE_SUCCESS="success";
	public final static String STATE_WAITING="waiting";
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.MERGE)
	protected List<OperationLog> operationList = new ArrayList<OperationLog>();
	
	@ManyToOne
	protected OperationLog parent;
	//--------------------------------------------------------------------------------
	public void setWaiting() {
		this.status= STATE_WAITING;
	}
	public void rollback() {
		this.status= STATE_FAILED;
		
		if (parent!=null)
			this.parent.rollback();
	}
	public void commit() {
		if (this.status!= STATE_FAILED)
			this.status= STATE_SUCCESS;
	}
	public void addOp(OperationLog op) {
		operationList.add(op);
		op.setParent(this);
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getOpType() {
		return opType;
	}
	public  <T  extends DirectoryEntry & PersistentObject > Subject addSubject(T subject) {
		Subject sub = new Subject();
		sub.setClassName(subject.getClass().getCanonicalName());
		sub.setFqn(subject.getFQN().toString());
		sub.setId(subject.getObjectId());
		subjects.add(sub);
		
		return sub;
	}
	public String getStatus() {
		return status;
	}
	public long getObjectId() {
		return internalId;
	}
	public void setParent(OperationLog parent) {
		this.parent = parent;
	}
	public OperationLog getParent() {
		return parent;
	}
	public Set<Subject> getSubjects() {
		return subjects;
	}
	public List<OperationLog> getOperations() {
		return operationList;
	}
}
