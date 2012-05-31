/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.naming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Directory<E> {
    
    private FQN rootNameSpace = null;
    private Map<FQN,E> registeredObjects = new Hashtable<FQN, E>();
    private boolean paranoid = true;
	
	// Lifecycle event listeners information
	//--------------------------------------------------------------------------------------------
	public enum LifecycleEventType {DEPLOY, UNDEPLOY};
	Map<LifecycleEventType, List<LifecycleEventListener> > lifecycleListeners = new HashMap<LifecycleEventType, List<LifecycleEventListener> >();
	
    public Directory(FQN rootNameSpace) {                
        if(rootNameSpace == null)
            throw new IllegalArgumentException("Root name space cannot be null");        
        this.rootNameSpace = rootNameSpace;
    }
    
    public FQN getRootNameSpace() {
        return rootNameSpace;
    }
    
    public boolean registerObject(FQN fqn, E object) {
        paranoidChecking(fqn);        
        
        if(!rootNameSpace.isSuperContextOf(fqn))    
             throw new IllegalArgumentException("Cannot register " + fqn + ", is not inside root name space " + rootNameSpace);
        if(object == null)
            throw new IllegalArgumentException("Cannot register null objects, fqn is " + fqn);
        registeredObjects.put(fqn, object);      
        return rootNameSpace.addChild(fqn);
    }            
    
    public boolean isNameRegistered(FQN fqn) {
        paranoidChecking(fqn);
        return registeredObjects.containsKey(fqn);
    }
    
    public boolean isObjectRegistered(E object) {
        return registeredObjects.containsValue(object);
    }
    
    public DirectoryEntry getObject(FQN fqn) {
        paranoidChecking(fqn);
        return (DirectoryEntry) registeredObjects.get(fqn);
    }    
    
    public Set<FQN> matchingNamesRegistered(FQN regExpFQN) {
        paranoidChecking(regExpFQN);
        Set<FQN> fqns = new HashSet<FQN>();
        Set<FQN> matchingFqns = rootNameSpace.getMatching(regExpFQN);
        for(FQN fqn : matchingFqns)
        	if(registeredObjects.containsKey(fqn))
        		fqns.add(fqn);
        return fqns;
    }
    
    public Set<FQN> matchingNamesAndChildrenRegistered(FQN regExpFQN) {
        paranoidChecking(regExpFQN);
        Set<FQN> fqns = new HashSet<FQN>();
        List<FQN> matchingFqns = new ArrayList<FQN>(rootNameSpace.getMatching(regExpFQN));        
        //fqns.addAll(matchingFqns);
        while(!matchingFqns.isEmpty()) {
        	FQN fqn = matchingFqns.remove(0);
        	if(registeredObjects.containsKey(fqn))
        		fqns.add(fqn);
        	List<FQN> children = fqn.getChildren();
        	for(FQN childrenFQN : children)
        		matchingFqns.add(childrenFQN);
        }
        return fqns;
    }
    
    public List<E> removeMatchingNames(FQN regExpFQN) {
        paranoidChecking(regExpFQN);
        List<E> objects = new ArrayList<E>();
        Set<FQN> matching = rootNameSpace.getMatching(regExpFQN);
        for(FQN name : matching) {
            if(!rootNameSpace.removeChild(name))
                throw new Error("Cannot remove child " + name + " but was match of " + regExpFQN);
            if(registeredObjects.get(name) == null)
                throw new Error("Found FQN " + name + " in hashmap that is not registered in directory!");
            objects.add(registeredObjects.remove(name));
        }
        return objects;
    }
    
    public List<E> getObjects(FQN regExpFQN) {
        paranoidChecking(regExpFQN);
        List<E> objects = new ArrayList<E>();
        Set<FQN> matching = rootNameSpace.getMatching(regExpFQN);
        for(FQN name : matching) {
            if(registeredObjects.get(name) == null)
                throw new Error("Found FQN " + name + " in hashmap that is not registered in directory!");
            objects.add(registeredObjects.get(name));
        }
        return objects;
    }
    
    private void paranoidChecking(FQN regExpFQN) {        
        
        if(paranoid) {                  
            Set<FQN> matching = rootNameSpace.getMatching(regExpFQN);
            for(FQN name : matching) {               
                if(registeredObjects.containsKey(name) && !rootNameSpace.isParent(name))
                    throw new Error("Unconsistence, the hashmap contains " + name + ", but not the name space");
            }
        }
    }
    
    public void printNameSpace() {
        printNameSpace(rootNameSpace);
    }
    private void printNameSpace(FQN name) {
        System.out.println(name);
        for(FQN child : name.getChildren())
            printNameSpace(child);
    }
    
	public void addLifecycleListener(LifecycleEventListener listener) {
		if (!lifecycleListeners.containsKey(listener.getEventType()))
			lifecycleListeners.put(listener.getEventType(), new ArrayList<LifecycleEventListener>());
		
		lifecycleListeners.get(listener.getEventType()).add(listener);
	}
	
	public void postEvent(LifecycleEventType type, FQN subject, Map<String, Object> parameters) {
		
		List<LifecycleEventListener> listListeners = lifecycleListeners.get(type);
		
		if (listListeners!=null) {
			ListIterator<LifecycleEventListener> itr = listListeners.listIterator();
			
			while (itr.hasNext()) {
				LifecycleEventListener listener = itr.next();
				
				if ((!listener.isStrict()&&listener.getSubject().isParent(subject)) || 
					    (listener.isStrict()&&listener.getSubject().equals(subject))) {
						
						listener.receiveEvent(parameters);
						if (listener.isDirty())
							itr.remove();
					}
			}
		}
	}
	
	public void removeLifecycleListener(LifecycleEventListener listener) {
		lifecycleListeners.get(listener.getEventType()).remove(listener);
	}
}
