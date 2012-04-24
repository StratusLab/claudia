package com.telefonica.euro_iaas.placement.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.io.ResourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.telefonica.euro_iaas.placement.dao.CloudProviderDao;
import com.telefonica.euro_iaas.placement.exception.EntityNotFoundException;
import com.telefonica.euro_iaas.placement.model.application.ServiceApplication;
import com.telefonica.euro_iaas.placement.model.application.VDC;
import com.telefonica.euro_iaas.placement.model.application.VEERequired;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.euro_iaas.placement.model.provider.Location;
import com.telefonica.euro_iaas.placement.model.provider.MemoryConf;
import com.telefonica.euro_iaas.placement.model.provider.VEE;

public class ProviderServiceTest {
	
	ProviderService providerService;
	
	@Before
	public void setUp() {
		List<CloudProvider> listCloudProviders = getCloudProviders();
		
		CloudProviderDao cloudProviderDao = mock(CloudProviderDao.class);
		when(cloudProviderDao.findAll()).thenReturn(listCloudProviders);
		
		KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent( "MyAgent" );
		kagent.applyChangeSet( ResourceFactory.newClassPathResource("changeset.xml"));
		
		providerService = new ProviderService();

		providerService.setCloudProviderDao(cloudProviderDao);
		providerService.setkAgent(kagent);
		
	}
	
	private List<CloudProvider> getCloudProviders() {
		List<CloudProvider> listCloudProvider = new Vector<CloudProvider>();
		
		listCloudProvider.add(getCloudProvider("Amazon", 10, 10));
		listCloudProvider.add(getCloudProvider("FlexiScale", 20, 20));
		listCloudProvider.add(getCloudProvider("Dropbox", 5, 40));
		listCloudProvider.add(getCloudProvider("Terabox", 100, 100));
		return  listCloudProvider;
		
	}
	

	private CloudProvider getCloudProvider(String name, int memory, int cost) {
		CloudProvider cp = new CloudProvider();
		
		Location loccp1 = new Location();
		loccp1.setName("Madrid");
		
		VEE vee1cp1 = new VEE();
		vee1cp1.setName("VEE1" + name);
		vee1cp1.setCost(cost);
		
		MemoryConf memconfvee1cp1 = new MemoryConf();
		memconfvee1cp1.setQuantity(memory);
		vee1cp1.setMemoryConf(memconfvee1cp1);
		
		VEE vee2cp1 = new VEE();
		vee2cp1.setName("VEE2" + name);
		vee2cp1.setCost(1);
		
		MemoryConf memconfvee2cp1 = new MemoryConf();
		memconfvee2cp1.setQuantity(1);
		vee2cp1.setMemoryConf(memconfvee2cp1);		
		
		cp.setLocation(loccp1);
		cp.setUri("http://www." + name + ".com");
		cp.setName(name);
		cp.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp1, vee2cp1})));
		vee1cp1.setCloudProvider(cp);
		vee2cp1.setCloudProvider(cp);
		
		return cp;
	}

	private VDC getVDC(int memory) {
		ServiceApplication as1vdc = new ServiceApplication();

		MemoryConf memconfvee1as1vdc = new MemoryConf();
		memconfvee1as1vdc.setQuantity(memory);

		VEERequired vee1as1vdc = new VEERequired();
		vee1as1vdc.setMemoryConf(memconfvee1as1vdc);
		vee1as1vdc.setName("veeRequired 1");
		
		MemoryConf memconfvee2as1vdc = new MemoryConf();
		memconfvee2as1vdc.setQuantity(1);
		
		VEERequired vee2as1vdc = new VEERequired();
		vee2as1vdc.setMemoryConf(memconfvee2as1vdc);
		vee2as1vdc.setName("veeRequired 2");		

		as1vdc.setVeesRequired(new HashSet<VEERequired>(Arrays.asList(new VEERequired[] { vee1as1vdc, vee2as1vdc})));
		vee1as1vdc.setServiceApplication(as1vdc);
		vee2as1vdc.setServiceApplication(as1vdc);

		VDC vdc = new VDC();
		vdc.setName("Telefonica");
		as1vdc.setvDC(vdc);

		vdc.setServiceApplications(new HashSet<ServiceApplication>(Arrays
				.asList(new ServiceApplication[] { as1vdc })));

		return vdc;
	}
	
	@Test
	public void testNotMatch() {
		CloudProvider bestCloudProvider = null;
		
		try {
			bestCloudProvider = providerService.getBestCloudProvider(getVDC(120));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		Assert.assertNull(bestCloudProvider);
	}

	@Test
	public void testBestMatch() {
		CloudProvider bestCloudProvider = null;
		
		try {
			bestCloudProvider = providerService.getBestCloudProvider(getVDC(10));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(bestCloudProvider);
	}
	
	
	@Test
	public void testManyMatches() {
		List<CloudProvider> cloudProviders = null;
		
		try {
			cloudProviders =  providerService.getAllowedCloudProviders(getVDC(10));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(cloudProviders);
		Assert.assertTrue(cloudProviders.size() > 1);
	}
	
}
