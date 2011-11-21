package com.telefonica.claudia.slm.monitoring;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.telefonica.claudia.slm.common.PersistentObject;
import com.telefonica.claudia.slm.naming.DirectoryEntry;
import com.telefonica.claudia.slm.naming.FQN;

@Entity
public class nodedirectory extends DirectoryEntry implements PersistentObject {

	public static final int STATUS_ACTIVE=1;
	public static final int STATUS_DELETED=2;
	
	public static final int TYPE_VDC=1;
	public static final int TYPE_SERVICE=2;
	public static final int TYPE_VEE=3;
	public static final int TYPE_REPLICA=4;
	public static final int TYPE_NIC=5;
	
	@Id
	@GeneratedValue
	private long internalId;
	
	private long internalNodeId;
	private int status;
	private Date fechaCreacion;
	private Date fechaBorrado;
	private int tipo;
	
	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
	private Set<nodedirectory> descendants = new HashSet<nodedirectory>();
	
	@ManyToOne
	private nodedirectory parent;

	public void setInternalNodeId(long internalNodeId) {
		this.internalNodeId = internalNodeId;
	}

	public long getInternalNodeId() {
		return internalNodeId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaBorrado(Date fecha_borrado) {
		this.fechaBorrado = fecha_borrado;
	}

	public Date getFechaBorrado() {
		return fechaBorrado;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getTipo() {
		return tipo;
	}
	
	public void setType(int tipo) {
		this.tipo = tipo;
	}

	public int getType() {
		return tipo;
	}
	


	public void setDescendants(Set<nodedirectory> descendants) {
		this.descendants = descendants;
	}

	public Set<?> getDescendants() {
		return descendants;
	}
	
	@SuppressWarnings("unchecked")
	public Set<?> getAllDescendants() {
		
		Set<nodedirectory> nodes= new HashSet<nodedirectory>();
		Set<nodedirectory> toBeProcessed = new HashSet<nodedirectory>();
		Set<nodedirectory> nextLevel = new HashSet<nodedirectory>();
		
		toBeProcessed.addAll(descendants);
		
		while (!toBeProcessed.isEmpty()) {
			nextLevel.clear();
			
			for (nodedirectory nd: toBeProcessed) {
				nextLevel.addAll((Set<? extends nodedirectory>) nd.getDescendants());
			}
			
			nodes.addAll(toBeProcessed);
			toBeProcessed.clear();
			toBeProcessed.addAll(nextLevel);
		}
		
		return nodes;
	}
	
	public void addDescendant(nodedirectory node) {
		descendants.add(node);
	}

	public long getObjectId() {
		return internalId;
	}

	public void setParent(nodedirectory parent) {
		this.parent = parent;
	}

	public nodedirectory getParent() {
		return parent;
	}

	@Override
	public FQN getFQN() {
		return new FQN(getFqnString());
	}
}
