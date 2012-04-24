package com.telefonica.euro_iaas.placement.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * 
 * @author jicarretero
 *
 */
public class WebAppBuilder {	
	private final static Logger log=Logger.getLogger(WebAppBuilder.class.getName());
	
	/**
	 * Default environment configuration file for a Web Application Context.
	 */
	public final static String DEFAULT_CONTEXT_FILE="jetty-env.xml";
	/**
	 * Web Application Context. 
	 */
	private WebAppContext context;
	
	/**
	 * Context configuration file.
	 */
	private String contextConfigFile;
	
	/**
	 * Default constructor for WebApp
	 */
	public WebAppBuilder() {
		log.log(Level.INFO,"Default constructor");
		context=new WebAppContext();
		context.setCompactPath(true);
		context.setParentLoaderPriority(true);
		contextConfigFile=null;
	}
	
	/**
	 * Gets the WebAppContext for this builder. 
	 * @return
	 */
	public WebAppContext getContext() {
		return context;
	}
	
	/**
	 * Mandatory last step to configure a server with this webApBuilder. Call this just before 
	 * ServerBuilder.listen() method.
	 * 
	 * @throws Exception 
	 */
	public void configure() 
	throws Exception {
		if (contextConfigFile!=null) {
			log.log(Level.FINE,"Configuring the Web Application Context");
			XmlConfiguration envConfiguration=new XmlConfiguration(
					new File(contextConfigFile).toURI().toURL());
			envConfiguration.configure(context);
		}			
	}	
	
	/**
	 * Sets the context path for this context;
	 * @param path
	 */
	public void setContextPath(String path) {
		context.setContextPath(path);
	}
	
	/**
	 * Sets a WarFile to deal with as a Context (instead of other things).
	 * @param warFile
	 */
	public void setWarFile(String warFile) {
		context.setWar(warFile);
	}
	
	/**
	 * Sets the ResourceBase of the WAR directory where the APP is deployed.
	 * Not compatible with setWarFile.
	 * @param base
	 */
	public void setResourceBase(String base) {
		File whatsBase=new File(base);
		if (whatsBase.isFile()) {
			setWarFile(base);
		} else if (whatsBase.isDirectory()) {
			context.setResourceBase(base);
			if ( (new File(base+"/WEB-INF/web.xml")).exists() )
				context.setDefaultsDescriptor(base+"/WEB-INF/web.xml");
		}
	}

	/**
	 * Sets the configuration context file. By default jetty-env.xml.
	 * @param contextConfigFile
	 */
	public void setContextConfigFile(String contextConfigFile) {
		this.contextConfigFile = contextConfigFile;
	}

	/**
	 * Return the name of the contextConfigFile;
	 * @return
	 */
	public String getContextConfigFile() {
		return contextConfigFile;
	}	
	
}
