package com.telefonica.euro_iaas.placement.init;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.telefonica.euro_iaas.placement.dao.CloudProviderDao;
import com.telefonica.euro_iaas.placement.helper.XmlCloudProviderHelper;
import com.telefonica.euro_iaas.placement.model.provider.CloudProvider;
import com.telefonica.euro_iaas.placement.model.provider.Location;
import com.telefonica.euro_iaas.placement.model.provider.MemoryConf;
import com.telefonica.euro_iaas.placement.model.provider.VEE;

@Service
public class InitDbCloudProviders{
	
	private static final Logger LOGGER = Logger.getLogger(InitDbCloudProviders.class.getName());
	
	@Autowired
	private CloudProviderDao cloudProviderDao;

	/* -- ESTO NO FUNCIONA - NO EXISTE DICHO FICHERO!!! 
	 * public static void main(final String[] args) throws Exception {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		ctx.registerShutdownHook();
		
		InitDbCloudProviders init = ctx.getBean(InitDbCloudProviders.class);
		init.load();
		//init.loadTestBedBCN();
	}*/	
	
	public InitDbCloudProviders() {
	}
	
	@Transactional
	public void loadTestBedBCN(){
		LOGGER.info("  ");

		assert TransactionSynchronizationManager.isActualTransactionActive();
		List<CloudProvider> list = cloudProviderDao.findAll();
		
		LOGGER.info(" size of current cloudProviders:  " + list.size());

        if (list.size() != 0){
        	LOGGER.info(" ALREADY LOADED!! ");
        	return;
        }
        /** 
		VEE vee1cp1 = new VEE();
		vee1cp1.setName("VEE1 EMOTIVE1");
		vee1cp1.setCost(10);
		MemoryConf mc=new MemoryConf();
		mc.setMemoryUnit(MemoryUnit.GB);
		mc.setQuantity(128);
		vee1cp1.setMemoryConf(mc);
		
        
        CloudProvider cp1 = new CloudProvider();
		cp1.setUri("http://84.21.173.26:8080/DRP");
		cp1.setName("EMOTIVE1");
		cp1.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp1})));
		vee1cp1.setCloudProvider(cp1);				
		
		VEE vee1cp2 = new VEE();
		vee1cp2.setName("VEE1 EMOTIVE2");
		vee1cp2.setCost(20);		
		
        CloudProvider cp2 = new CloudProvider();
		cp2.setUri("http://84.21.173.27:8080/DRP");
		cp2.setName("EMOTIVE2");
		cp2.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp2})));
		vee1cp2.setCloudProvider(cp2);			

		
		VEE vee1cp3 = new VEE();
		vee1cp3.setName("VEE1 ONE");
		vee1cp3.setCost(30);	

        CloudProvider cp3 = new CloudProvider();
		cp3.setUri("http://84.21.173.28:4567");
		cp3.setName("ONE");
		cp3.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp3})));
		vee1cp3.setCloudProvider(cp3);
		// * ----------- temporal ------------------- *
		try {
			XMLEncoder encoder=new XMLEncoder(
					new BufferedOutputStream(new FileOutputStream("/tmp/mf.xml")));			
			encoder.writeObject(cp1);
			encoder.writeObject(vee1cp1);
			encoder.writeObject(cp2);
			encoder.writeObject(vee1cp2);
			encoder.writeObject(cp3);
			encoder.writeObject(vee1cp3);
			encoder.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// * ----------- fin temporal ------------------- *
        
		cloudProviderDao.save(cp1);
		cloudProviderDao.save(cp2);
		cloudProviderDao.save(cp3);

		
		LOGGER.info("loadTestBedBCN load end");
        

		**/
        /**/XmlCloudProviderHelper x=XmlCloudProviderHelper.getInstance();
        
		try {
			Set<CloudProvider> newcps=x.decode();
			for (CloudProvider cp : newcps) {
				System.out.println("cn.nombre: " + cp.getName());
				for (VEE vee : cp.getVees()) {
					System.out.println("....vee.nombre: "+vee.getName() + " --> " + vee.getCost());
				}
				System.out.flush();
				cloudProviderDao.save(cp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedDataTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			/**/
	}
	
	@Transactional
	public void load(){
		assert TransactionSynchronizationManager.isActualTransactionActive();
		
		List<CloudProvider> list = cloudProviderDao.findAll();
		
		LOGGER.info(" size of current cloudProviders:  " + list.size());

        if (list.size() != 0){
        	LOGGER.info(" ALREADY LOADED!! ");
        	return;
        }
        
		LOGGER.info("load start");

		

		Location loccp1 = new Location();
		loccp1.setName("Madrid");
		
		VEE vee1cp1 = new VEE();
		vee1cp1.setName("VEE1 Amazon");
		vee1cp1.setCost(10);
		
		MemoryConf memconfvee1cp1 = new MemoryConf();
		memconfvee1cp1.setQuantity(5);
		vee1cp1.setMemoryConf(memconfvee1cp1);
		
		VEE vee2cp1 = new VEE();
		vee2cp1.setName("VEE2 Amazon");
		vee2cp1.setCost(10);
		
		MemoryConf memconfvee2cp1 = new MemoryConf();
		memconfvee2cp1.setQuantity(20);
		vee2cp1.setMemoryConf(memconfvee2cp1);		
		
		CloudProvider cp1 = new CloudProvider();
		cp1.setLocation(loccp1);
		cp1.setUri("http://www.amazon.com");
		cp1.setName("Amazon");
		cp1.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp1, vee2cp1})));
		vee1cp1.setCloudProvider(cp1);
		vee2cp1.setCloudProvider(cp1);
		
		Location loccp2 = new Location();
		loccp2.setName("Barcelona");
		
		VEE vee1cp2 = new VEE();
		vee1cp2.setName("VEE1 RackSpace");
		vee1cp2.setCost(20);
		
		MemoryConf memconfvee1cp2 = new MemoryConf();
		memconfvee1cp2.setQuantity(256);
		vee1cp2.setMemoryConf(memconfvee1cp2);
		
		VEE vee2cp2 = new VEE();
		vee2cp2.setName("VEE2 RackSpace");
		vee2cp2.setCost(20);
		
		MemoryConf memconfvee2cp2 = new MemoryConf();
		memconfvee2cp2.setQuantity(40);
		vee2cp2.setMemoryConf(memconfvee2cp2);
		
		CloudProvider cp2 = new CloudProvider();
		cp2.setName("RackSpace");
		cp2.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp2, vee2cp2})));
		cp2.setLocation(loccp2);
		cp2.setUri("http://www.rackspace.com");
		vee1cp2.setCloudProvider(cp2);
		vee2cp2.setCloudProvider(cp2);
		
		Location loccp3 = new Location();
		loccp3.setName("NY");

		VEE vee1cp3 = new VEE();
		vee1cp3.setName("VEE1 FlexiScale");
		vee1cp3.setCost(1);
		
		MemoryConf memconfvee1cp3 = new MemoryConf();
		memconfvee1cp3.setQuantity(256);
		vee1cp3.setMemoryConf(memconfvee1cp3);
		
		VEE vee2cp3 = new VEE();
		vee2cp3.setName("VEE2 FlexiScale");
		vee2cp3.setCost(100);
		
		MemoryConf memconfvee2cp3 = new MemoryConf();
		memconfvee2cp3.setQuantity(5);
		vee2cp3.setMemoryConf(memconfvee2cp3);		
		
		CloudProvider cp3 = new CloudProvider();
		cp3.setName("FlexiScale");
		cp3.setUri("http://www.flexiscale.com");
		cp3.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp3, vee2cp3})));
		cp3.setLocation(loccp3);
		vee1cp3.setCloudProvider(cp3);
		vee2cp3.setCloudProvider(cp3);
		
		Location loccp4 = new Location();
		loccp4.setName("Paris");
		
		VEE vee1cp4 = new VEE();
		vee1cp4.setName("VEE1 OpenNebula");
		vee1cp4.setCost(100);
		
		MemoryConf memconfvee1cp4 = new MemoryConf();
		memconfvee1cp4.setQuantity(100);
		vee1cp4.setMemoryConf(memconfvee1cp4);
		
		CloudProvider cp4 = new CloudProvider();
		cp4.setName("OpenNebula");
		cp4.setUri("http://www.opennebula.com");
		cp4.setVees(new HashSet<VEE>(Arrays.asList(new VEE[] {vee1cp4})));
		cp4.setLocation(loccp4);
		vee1cp4.setCloudProvider(cp4);
		
		cloudProviderDao.save(cp1);
		cloudProviderDao.save(cp2);
		cloudProviderDao.save(cp3);
		cloudProviderDao.save(cp4);
		
		LOGGER.info("load end");
	}

	/**
	 * @return the cloudProviderDao
	 */
	public CloudProviderDao getCloudProviderDao() {
		return cloudProviderDao;
	}

	/**
	 * @param cloudProviderDao the cloudProviderDao to set
	 */
	public void setCloudProviderDao(CloudProviderDao cloudProviderDao) {
		this.cloudProviderDao = cloudProviderDao;
	}



}
