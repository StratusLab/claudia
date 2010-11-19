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
package com.telefonica.claudia.slm.naming;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Directory<E> {
    
    private FQN rootNameSpace = null;
    private Map<FQN,E> registeredObjects = new Hashtable<FQN, E>();
    private boolean paranoid = true;
    
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
    
    public Object getObject(FQN fqn) {
        paranoidChecking(fqn);
        return registeredObjects.get(fqn);
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
        	Set<FQN> children = fqn.getChildren();
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
    
}