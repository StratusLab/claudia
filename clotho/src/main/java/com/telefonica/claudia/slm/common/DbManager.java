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
package com.telefonica.claudia.slm.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DbManager {

	private EntityManager em;

	private static DbManager instance=null;	
	
	public static DbManager getDbManager() {
		if (instance==null) {
			instance = new DbManager();
		}
		
		return instance;
	}
	
	private DbManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ClaudiaPU");
		em = emf.createEntityManager();
	}
	
	public <T> void save(T object) {
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		em.persist(object);
		
		t.commit();
	}
	
	public <T> void remove(T object) {
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		em.remove(object);
		
		t.commit();
	}
	
	public <T> T get(Class<T> objectClass, Object primaryKey) {
		EntityTransaction t =em.getTransaction();
		
		T result;
		
		t.begin();
		result = (T) em.find(objectClass, primaryKey);
		t.commit();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> objectClass) {
		
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		Query queryResult = em.createQuery("from " + objectClass.getName());
		
		t.commit();
		
		return (List<T>) queryResult.getResultList();
	}
}
