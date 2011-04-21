package com.telefonica.claudia.configmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidClassException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telefonica.claudia.configmanager.lb.LoadBalancerConfigurator;
import com.telefonica.claudia.configmanager.lb.impl.LoadBalancerConfiguratorRESTImpl;




/**
 * This class constructs and returns different {@link Configurator}
 * implementations depending on configuration and service container
 * implementations.
 * 
 * The default constructor loads a properties file called configurators.prop
 * from the classpath. If metioned file is not present, default properties are
 * automatically loaded. See {@link ConfiguratorFactory#loadDefaultProperties()}
 * 
 * This class is a singleton. For getting an instance,
 * {@link ConfiguratorFactory#getInstance()} should be invoked.
 * 
 * @author amartin
 * 
 */
public class ConfiguratorFactory {

	private static ConfiguratorFactory instance;
	private Properties prop;
	private Logger log;

	/**
	 * Private constructor
	 */
	private ConfiguratorFactory() {
		this.log = Logger.getLogger(this.getClass());

		ClassLoader loader = ClassLoader.getSystemClassLoader();
		this.prop = new Properties();
		
		try{
			this.prop.load(new FileInputStream(new File("configurators.properties")));
		}
		catch(Exception e){
			log.info("Unable to load properties from file configurators.properties", e);
			this.prop = null;
		}

		if (this.prop == null) {
			this.log.info("Trying to load properties from classpath");
			try {
				this.prop.load(loader.getResourceAsStream("configurators.properties"));
			} catch (Throwable e) {
				this.log.warn("Unable to load properties from classpath. Using defaults", e);
				this.loadDefaultProperties();
			}
		}
	}

	/**
	 * Loads default properties
	 */
	private void loadDefaultProperties() {
		this.prop = new Properties();
		this.prop.setProperty(
				LoadBalancerConfigurator.class.getCanonicalName(),
				LoadBalancerConfiguratorRESTImpl.class.getCanonicalName());

		

		this.prop.setProperty("OVFEnvironmentFilePath", "/home/amartin/ovfTemplate.xml");
		this.prop.setProperty("outputVJSCConfigFilePath", "/home/amartin/output.xml");
		this.prop.setProperty("DatabaseIP", "org.apache.jackrabbit.core.fs.db.DbFileSystem.IPdatabase");
		this.prop.setProperty("DatabaseUser", "org.apache.jackrabbit.core.fs.db.DbFileSystem.user");
		this.prop.setProperty("DatabaseURL", "org.apache.jackrabbit.core.fs.db.DbFileSystem.url");
		this.prop.setProperty("DatabasePassword", "org.apache.jackrabbit.core.fs.db.DbFileSystem.password");
		this.prop.setProperty("DatabaseType", "org.apache.jackrabbit.core.fs.db.DbFileSystem.schema");

	}

	/**
	 * Creates an instance (if not created already) and returns it.
	 * 
	 * @return unique instance
	 */
	public static ConfiguratorFactory getInstance() {
		if (instance == null) {
			instance = new ConfiguratorFactory();
		}
		return instance;
	}

	/**
	 * Constructs the propper instance of a given class or interface extending
	 * {@link Configurator}. Correct properties must be loaded in order to
	 * construct objects correctly.
	 * 
	 * In the mentioned properties, there has to be a property which key must be
	 * named exactly as the canonical name of the class passed as an argument.
	 * The value of this property must be the canonical name of a class
	 * implementing the property's key.
	 * 
	 * @param <T>
	 *            Class implementing {@link Configurator}
	 * @param c
	 *            Indicates which interface must implement or class must extend
	 *            the constructed object
	 * @return Object implementing given class
	 * @throws InvalidClassException
	 *             when it is impossible to guess from properties which class to
	 *             instantiate from given class/interface.
	 */
	public <T extends Configurator> T createConfigManager(Class<T> c)
			throws InvalidClassException {

		String className = this.prop.getProperty(c.getCanonicalName());

		try {
			Configurator cfg = (Configurator) Class.forName(className).newInstance();
			
			cfg.setProperties(this.prop);

			return (T) cfg;
		} catch (Exception e) {
			String msg = "Unable to instantiate " + className
					+ " corresponding to " + c.getCanonicalName();
			this.log.fatal(msg, e);
			throw new InvalidClassException(msg, e.getLocalizedMessage());
		}
	}
}
