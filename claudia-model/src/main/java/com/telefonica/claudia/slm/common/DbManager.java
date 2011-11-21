/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DbManager {

	protected static final String DEFAULT_URL="";
	protected static final String DEFAULT_USER="claudia";
	protected static final String DEFAULT_PASSWORD="ClaudiaPass";
	
		
	protected EntityManagerFactory emf;

	protected static DbManager instance=null;	
	
	public static DbManager getDbManager() {
		if (instance==null) {
			instance = new DbManager(DEFAULT_URL, true, DEFAULT_USER, DEFAULT_PASSWORD);
		}
		
		return instance;
	}
	
	public static DbManager createDbManager(String DBUrl, boolean recreate, String user, String password) {
		if (instance==null) {		
			instance = new DbManager(DBUrl, recreate, user, password);
		}
		
		return instance;
	}
	
	protected DbManager(String DBUrl, boolean recreate, String user, String password) {
		Map <String, Object> configuration = new HashMap<String, Object>();
		configuration.put("hibernate.connection.url", DBUrl);
		configuration.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.put("hibernate.connection.username", user);
		configuration.put("hibernate.connection.password", password);
		configuration.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		
		configuration.put("hibernate.c3p0.min_size", "5");
		configuration.put("hibernate.c3p0.max_size", "50");
		configuration.put("hibernate.c3p0.timeout", "5000");
		configuration.put("hibernate.c3p0.max_statements", "100");

		if (recreate)
			configuration.put("hibernate.hbm2ddl.auto", "create-drop");

		emf = Persistence.createEntityManagerFactory("ClaudiaPU", configuration);
	}
	
	public void save(PersistentObject object) {
		EntityManager em = getEntityManager();
		
		EntityTransaction t =em.getTransaction();
		t.begin();
		
		try {
			PersistentObject o = (PersistentObject) em.find(object.getClass(), object.getObjectId());
			
			if (o==null)
				em.persist(object);
			else {
				em.merge(object);
			}
			
			t.commit();
		} catch (Error e) {
			t.rollback();
			em.clear();
			em.close();
			throw e;
		}
	}
	
	public <T extends PersistentObject> void save(Set<T> set) {
		EntityManager em = getEntityManager();
		
		EntityTransaction t =em.getTransaction();
		t.begin();
		
		try {
			Iterator<T> iterator = set.iterator();
			while (iterator.hasNext()) {
				T object = (T) iterator.next();

				PersistentObject o = (PersistentObject) em.find(object.getClass(), object.getObjectId());
				if (o==null)
					em.persist(object);
				else {
					em.merge(object);
				}				
			}
			
			t.commit();
		} catch (Error e) {
			t.rollback();
			em.clear();
			em.close();
			throw e;
		}
	}	
	
	public synchronized EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public synchronized void remove(PersistentObject object) {
		EntityManager em= emf.createEntityManager();
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		
		try {
			PersistentObject o = (PersistentObject) em.find(object.getClass(), object.getObjectId());
			
			if (o!= null)
				em.remove(o);
			
			t.commit();
		} catch (Error e) {
			t.rollback();
			
			throw e;
		} finally {
			em.close();
		}
	}
	
	public synchronized <T> T get(Class<T> objectClass, Object primaryKey) {
		EntityManager em= emf.createEntityManager();
		EntityTransaction t =em.getTransaction();
		
		T result;
		
		t.begin();
		
		try {
			result = (T) em.find(objectClass, primaryKey);
			t.commit();
			
			return result;
		} catch (Error e) {
			t.rollback();
			
			throw e;
		} finally {
			em.clear();
			em.close();
		}	
	}
	
	public synchronized <T> T get(Class<T> objectClass, String fqn) {
		EntityManager em= emf.createEntityManager();
		EntityTransaction t =em.getTransaction();
		
		T result=null;
		t.begin();
		
		try {
			System.out.println("from " + objectClass.getName() + " where fqn='" + fqn + "'");
			Query queryResult = em.createQuery("from " + objectClass.getName() + " where fqn='" + fqn + "'");
			
			List<T> resultList = (List<T>) queryResult.getResultList();
			
			for (T resultCandidate: resultList) {
				result = resultCandidate;
			}
				
			t.commit();

			return result;
			
		} catch (Error e) {
			t.rollback();
			
			throw e;
		} finally {
			em.clear();
			em.close();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T> List<T> getList(Class<T> objectClass) {
		EntityManager em= emf.createEntityManager();
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		
		try {
			Query queryResult = em.createQuery("from " + objectClass.getName());
			
			List<T> result = (List<T>) queryResult.getResultList();
			
			for (T iter: result)
				em.refresh(iter);
			
			t.commit();
			
			return result;
		} catch (Error e) {
			t.rollback();
			
			throw e;
		} finally {
			em.clear();
			em.close();
		}
	}

	public synchronized <T> List<T> executeQueryList(Class<T> objectClass, String query) {
		EntityManager em= emf.createEntityManager();
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		
		try {
			Query queryResult = em.createQuery(query);
			
			List<T> result = (List<T>) queryResult.getResultList();
			
			for (T iter: result)
				em.refresh(iter);
			
			t.commit();
			
			return result;
		} catch (Error e) {
			t.rollback();
			
			throw e;
		} finally {
			em.clear();
			em.close();
		}
	}	
	
	public synchronized Object executeQuery(String query) {
		EntityManager em= emf.createEntityManager();
		EntityTransaction t =em.getTransaction();
		
		t.begin();
		
		try {
			Query queryResult = em.createQuery(query);
			
			Object result = queryResult.getSingleResult();
			
			t.commit();
			
			return result;
		} catch (Error e) {
			t.rollback();
			
			throw e;
		} finally {
			em.clear();
			em.close();
		}
	}	
}
