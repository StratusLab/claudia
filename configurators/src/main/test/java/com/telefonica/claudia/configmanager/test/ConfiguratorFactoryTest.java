package com.telefonica.claudia.configmanager.test;

import java.io.InvalidClassException;

import junit.framework.TestCase;

import com.telefonica.claudia.configmanager.ConfiguratorFactory;
import com.telefonica.claudia.configmanager.lb.LoadBalancerConfigurator;
import com.telefonica.claudia.configmanager.vjsc.VJSCConfigurator;

public class ConfiguratorFactoryTest extends TestCase {

	public void testGetInstance() {
		assertSame(ConfiguratorFactory.getInstance(), ConfiguratorFactory
				.getInstance());
	}

	public void testCreateConfigManager() {
		ConfiguratorFactory cFactory = ConfiguratorFactory.getInstance();

		VJSCConfigurator vjscConfig = null;
		try {
			vjscConfig = cFactory.createConfigManager(VJSCConfigurator.class);
		} catch (InvalidClassException e) {
			fail(e.getMessage());
		}

		LoadBalancerConfigurator lbConfig = null;
		try {
			lbConfig = cFactory
					.createConfigManager(LoadBalancerConfigurator.class);
		} catch (InvalidClassException e) {
			fail(e.getMessage());
		}

		assertTrue("vjscConfig is an expected class instance",
				VJSCConfigurator.class.isAssignableFrom(vjscConfig.getClass()));
		assertTrue("lbConfig is an expected class instance",
				LoadBalancerConfigurator.class.isAssignableFrom(lbConfig
						.getClass()));
	}
}
