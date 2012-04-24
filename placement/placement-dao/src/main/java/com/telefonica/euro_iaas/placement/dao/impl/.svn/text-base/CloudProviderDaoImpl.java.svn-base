package com.telefonica.euro_iaas.placement.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.telefonica.euro_iaas.placement.dao.CloudProviderDao;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;

@Repository
public class CloudProviderDaoImpl implements CloudProviderDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudProviderDaoImpl.class.getName());
	
    @PersistenceContext
    private EntityManager entityManager;


	@Override
	public List<CloudProvider> findAll() {
		@SuppressWarnings("unchecked")
		List<CloudProvider> result = entityManager.createQuery("SELECT cp from CloudProvider as cp").getResultList();
		return result;
	}


	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}


	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CloudProvider save(CloudProvider entity) {
        entityManager.persist(entity);
        return entity;
	}

}
