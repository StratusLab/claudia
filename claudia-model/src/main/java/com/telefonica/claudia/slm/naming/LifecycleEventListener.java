package com.telefonica.claudia.slm.naming;

import java.util.Map;

import com.telefonica.claudia.slm.naming.Directory.LifecycleEventType;

public abstract class LifecycleEventListener {

	private static long idCounter = 0;
	private long id;
	private boolean dirty=false;
	
	private LifecycleEventType eventType;
	
	/**
	 * FQN grouping all the subjects the listener is interested on. Whenever a
	 * new event of the desired type is thrown, the listener will be called 
	 * if the FQN of the subject is a descendant of the FQN of the desiredSubject.
	 */
	private FQN desiredSubject;
	private boolean strict;
	
	private static long getId() {
		return idCounter++;
	}
	
	public LifecycleEventType getEventType() {
		return eventType;
	}
	
	public FQN getSubject() {
		return desiredSubject;
	}
	
	/**
	 * Create a new listener for the selected event and subject. Two kind of behaviours can
	 * be selected when asking for a subject: call the listener only on events that match 
	 * the exact FQN (strict mode true), or trigger it with any descendant of the given FQN.
	 * 
	 * @param event
	 * 	Indicates the event type the listener is listening to.
	 * 
	 * @param subject
	 * 	FQN of the subject of the events to monitor. 
	 * 
	 * @param strict
	 * 	Controls whether only a exact FQN match will trigger the call (strict == true) or
	 *  any of its descendants will (strict == false).
	 */
	public LifecycleEventListener(LifecycleEventType event, FQN subject, boolean strict) {
		this.eventType = event;
		this.desiredSubject = subject;
		this.id=getId();
		this.setStrict(strict);
	}
	
	public abstract void receiveEvent(Map<String, Object> parameters);
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj!=null && obj instanceof LifecycleEventListener && id == ((LifecycleEventListener)obj).id)
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return (int) (id%10);
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public boolean isStrict() {
		return strict;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}
}
