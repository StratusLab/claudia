package com.telefonica.euro_iaas.placement.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * Helping Class to create a Jetty Server using jndi and other interesting features.
 * 
 * @author jicarretero
 *
 */
public class ServerBuilder {
	private static final Logger log=Logger.getLogger(ServerBuilder.class.getName());
	public final static String webAppHome=	
		"../placement-rest-api/target/placement-rest-api-0.0.1-SNAPSHOT";
	
	/**
	 * Set properties to enable jndi utilization.
	 */
	static {
		System.setProperty("java.naming.factory.url.pkgs",
			"org.eclipse.jetty.jndi");
		System.setProperty("java.naming.factory.initial",
			"org.eclipse.jetty.jndi.InitialContextFactory");
	}
	
	/** 
	 * The name of the default Jetty XML file.
	 */
	public final static String DEFAULT_JETTY_XML="jetty.xml";
	
	/**
	 * Puerto de la clase
	 */
	private Integer port;	
	
	/**
	 * Server configuration file (jetty.xml)
	 */
	private String serverConfigurationFile;
	
	/**
	 * Contexto de la aplicaci&oacute;n Web que funcionar&aacute; en este Jetty. 
	 */
	private WebAppContext context;
	
	/**
	 * Web Server
	 */
	private Server server;
	
	/**
	 * Constructor por defecto de la clase.
	 */
	ServerBuilder () {	
		this.serverConfigurationFile=null;
		this.port=null;
	}
	
	/**
	 * Sets the ServerBuilder port.	
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the ServerBuilder port.
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the WebAppContext for this Server. 
	 * @param context
	 */
	public void setContext(WebAppContext context) {
		this.context = context;
	}

	/**
	 * Gets the Web Application contex.
	 * @return context
	 */
	public WebAppContext getContext() {
		return context;
	}
	
	/**
	 * Sets the server for this context.
	 * @return
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * Sets the server configuration file
	 * @param serverConfigurationFile
	 */	
	public void setServerConfigurationFile(String serverConfigurationFile) {
		this.serverConfigurationFile = serverConfigurationFile;
	}

	/**
	 * 
	 * @return
	 */
	public String getServerConfigurationFile() {
		return serverConfigurationFile;
	}
	
	/**
	 * Sets the context and listens all in one.
	 * 
	 * @param context Web application context to be used.
	 * @throws Exception
	 */
	public void listen(WebAppContext context) throws Exception {
		setContext(context);
		listen();
	}
	/**
	 * Configures Jetty-Server and listens for HTTP Requets.
	 * @throws Exception 
	 */
	public void listen() throws Exception {
		log.log(Level.FINE, "Setting the server port", port);
		if (port==null){
			server = new Server();
		} else {
			server = new Server(port);
		}		
		
		if (serverConfigurationFile!=null) {
			log.log(Level.FINE, "Configuring jetty with file",serverConfigurationFile);
			XmlConfiguration envConfiguration=new XmlConfiguration(
					new File(serverConfigurationFile).toURI().toURL());
			envConfiguration.configure(server);
		}
		
		log.log(Level.FINE, "Setting the context handler");
		server.setHandler(context);
		
		log.log(Level.WARNING, "Starting Jetty server");
		server.start();
		server.join();
		log.log(Level.WARNING, "Jetty server finished");
	}
	/**
	 * Pa ir tirando....
	 * @param args
	 * @throws Exception
	 */
	public static void main(String [] args) throws Exception {
		PlacementJettyServer.newMain(args);
	}

}
