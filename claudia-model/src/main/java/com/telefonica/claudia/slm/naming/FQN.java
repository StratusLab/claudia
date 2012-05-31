/*

S  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.naming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.telefonica.claudia.slm.common.PersistentObject;

@Entity
public class FQN implements Serializable, PersistentObject {
	
	@Id
	@GeneratedValue
	public long internalId;
	
	private static final long serialVersionUID = 1L;
	public static final String CONTEXT_SEPARATOR = ".";
    public static final String ANY_NAME_SYMBOL = "*";
    
    private String[] contextsArray = null;
    private String fqnString = null;
    private int fqnHashCode = 0;
    
    @Column(name="regularExp")
    private boolean regExp = false;
    
    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
    private List<FQN> children = new ArrayList<FQN>();
    
    public FQN() {}
    
    public FQN(String fullName) {
        this(fullName.split(Pattern.quote(CONTEXT_SEPARATOR)));
    }
    
    public FQN(String[] contexts) {
        contextsArray = Arrays.copyOf(contexts, contexts.length);
        for(String context : contextsArray)
            if(context.equals(ANY_NAME_SYMBOL))
                regExp = true;
        
        if(contextsArray.length == 0) {
            fqnString = "";
        } else {        
            fqnString = contextsArray[0];
            for(int index = 1; index < contextsArray.length; index++)             
                fqnString += CONTEXT_SEPARATOR + contextsArray[index];      
        }
        
        fqnHashCode = fqnString.hashCode();
    }
    
    public boolean isRegExp() {
        return regExp;
    }
    
    public boolean addChild(FQN fqnChild) {
        
        if(fqnChild == null)
            throw new NullPointerException("Trying to add null Name as child of " + this);

        if(fqnChild.isRegExp())
            throw new IllegalArgumentException("Cannot add a regular expression as child");
        
        if(!isSuperContextOf(fqnChild))
            throw new IllegalArgumentException("Cannot add child " + fqnChild + " to name " + this);
        
        for(FQN child : children) {
            if(child.equals(fqnChild))
                return false;
            if(child.isSuperContextOf(fqnChild)) {
                return child.addChild(fqnChild);          
            }
        }        
        
        if(isInmediateSuperContexOf(fqnChild)) {
            children.add(fqnChild);
            return true;
        } else {
            String[] newChildContexts = fqnChild.contexts();
            FQN interChild = new FQN(Arrays.copyOf(newChildContexts, contextsArray.length+1));
            children.add(interChild);
            return interChild.addChild(fqnChild);
        }
        
    }
    
    public boolean removeChild(FQN fqnChild) {
        
        if(fqnChild == null)
            throw new NullPointerException("Trying to remove null Name as child of " + this);

        if(!isSuperContextOf(fqnChild))
            throw new IllegalArgumentException("Cannot remove child " + fqnChild + " from name " + this);        
        
        for(FQN child : children) {
            if(child.equals(fqnChild)) {
                children.remove(fqnChild);
                return true;
            }
            if(child.isSuperContextOf(fqnChild)) {
                return child.removeChild(fqnChild);          
            }
        }                
        
        return false;
    }
    
    public List<FQN> getChildren() {
        return children;
    }
    
    public boolean isParent(FQN fqn) {
        
        if(!isSuperContextOf(fqn))
            return false;
        
        if(children.contains(fqn))
            return true;
        
        for(FQN child : children)
            if(child.isParent(fqn))
                return true;
        
        return false;
    }
    
    public String[] contexts() {
        return Arrays.copyOf(contextsArray, contextsArray.length);
    }
    
    public int size() {
        return contextsArray.length;
    }
    
    public int commonSubContexts(FQN fqn) {
        
        int minLength = Math.min(contextsArray.length, fqn.size());        
        
        String[] nameContexts = fqn.contexts();
        int commonSubContexts = 0;
        for(int index = 0; index < minLength; index++) {
            String context = contextsArray[index];
            String nameContext = nameContexts[index];
            if( !(context.equals(ANY_NAME_SYMBOL)) &&
                !(nameContext.equals(ANY_NAME_SYMBOL)) &&    
                !(context.equals(nameContext)))
                break;
            commonSubContexts++;
        }
        
        return commonSubContexts;                
    }
    
    public boolean isSubContextOf(FQN fqn) {
        
        if(fqn.size() >= contextsArray.length)
            return false;        
        
        if(fqn.size() == 0)
            return true;
        
        return (commonSubContexts(fqn) == fqn.size());
    }
    
    public boolean isInmediateSubContextOf(FQN fqn) {
        
        if(fqn.size() != contextsArray.length - 1)
            return false;
        
        return (commonSubContexts(fqn) == contextsArray.length - 1);
    }
    
    public boolean isSuperContextOf(FQN fqn) {
        return fqn.isSubContextOf(this);
    }
    
    public boolean isInmediateSuperContexOf(FQN fqn) {
        return fqn.isInmediateSubContextOf(this);
    }
    
    public boolean matches(FQN fqn) {
        
        if(fqn.size() != contextsArray.length)
            return false;
        
        return (commonSubContexts(fqn) == contextsArray.length);
        
    }
    
    public Set<FQN> getMatching(FQN fqn) {
        
        Set<FQN> matchingNames = new HashSet<FQN>();
        
        if(fqn.matches(this)) {
            matchingNames.add(this);
            return matchingNames;
        }
        
        if(contextsArray.length < fqn.size()) {
            for(FQN child : getChildren())
                if(child.isSuperContextOf(fqn) || child.size() == fqn.size())
                    matchingNames.addAll(child.getMatching(fqn));
        }
        
        return matchingNames;
    }
    
    public boolean removeMatching(FQN fqn) {
    	
        boolean changed = false;
        for(FQN name : getMatching(fqn)) {
            if(!removeChild(name))
                throw new Error("Child " + name + " not removed of " + this + ", yet matches " + fqn);
            changed = true;
        }
        return changed;
    }
    
    public FQN getChild(FQN fqn) {
        
        Set<FQN> names = getMatching(fqn);
        if(getMatching(fqn).isEmpty())
            return null;
        
        return names.iterator().next();
    }
    
    public boolean isNameDefined(FQN fqn) {        
        return (getChild(fqn) != null);
    }
    
    @Override
    public boolean equals(Object object) {
        
        if(object == null)
            return false;
        
        if(!(object instanceof FQN))
            return false;
        
        String[] candContexts = ((FQN)object).contexts();
        
        if(candContexts.length != contextsArray.length)
            return false;
        
        for(int index = 0; index < contextsArray.length; index++)
            if(!contextsArray[index].equals(candContexts[index]))
                return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return fqnHashCode;
    }
    
    @Override
    public String toString() {
        return fqnString;
    }

	public long getObjectId() {
		return internalId;
	}
    
}
