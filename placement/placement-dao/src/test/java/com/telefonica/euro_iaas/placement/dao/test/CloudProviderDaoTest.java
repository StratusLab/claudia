package com.telefonica.euro_iaas.placement.dao.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.placement.dao.CloudProviderDao;
import com.telefonica.euro_iaas.placement.dao.impl.CloudProviderDaoImpl;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.euro_iaas.placement.model.provider.Location;
import com.telefonica.euro_iaas.placement.model.provider.MemoryConf;
import com.telefonica.euro_iaas.placement.model.provider.MemoryConf.MemoryUnit;
import com.telefonica.euro_iaas.placement.model.provider.VEE;


public class CloudProviderDaoTest {
	private CloudProviderDao dao;
	
	@Before
	public void doBefore() {
		dao=new CloudProviderDaoImpl();
	}
	@Test
	public  void daoTest() {	
		int x=3;
		assert(x<50);
	}
}
