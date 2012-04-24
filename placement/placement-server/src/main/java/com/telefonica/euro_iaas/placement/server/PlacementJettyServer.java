package com.telefonica.euro_iaas.placement.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class PlacementJettyServer {
	private static final Logger log=Logger.getLogger(PlacementJettyServer.class.getName());
	
	/***
	 * OldMain para hacer las pruebas - Hay que boorarlo.
	 * @param args
	 * @throws Exception
	 */
	public static void oldMain(String args[]) throws Exception {	
		log.log(Level.FINE, "Entrando en main()");
		
		Server server = new Server(8080);
		
		XmlConfiguration envConfiguration=new XmlConfiguration(
		    new File("src/main/resources/jetty-env.xml").toURI().toURL());
		//envConfiguration.setJettyEnvXml(url);
				
		XmlConfiguration configuration=new XmlConfiguration(new File("src/main/resources/jetty.xml").toURI().toURL());
		configuration.configure(server);
		
		
		WebAppContext webapp=new WebAppContext();
		webapp.setContextPath("/placement");		
		webapp.setCompactPath(true);		
	    webapp.setDefaultsDescriptor(ServerBuilder.webAppHome + "/WEB-INF/web.xml");
		webapp.setResourceBase(ServerBuilder.webAppHome);		
		webapp.setParentLoaderPriority(true);
		envConfiguration.configure(webapp);
		
		server.setHandler(webapp);
		server.start();
		server.join();		
	}
	
	/**
	 * New main para hacer las pruebas. Hay que borrarlo
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void newMain(String args[]) throws Exception {
		WebAppBuilder appBuilder=new WebAppBuilder();
		appBuilder.setResourceBase(ServerBuilder.webAppHome);
		appBuilder.setContextPath("/placement");
		appBuilder.setContextConfigFile("src/main/resources/jetty-env.xml");
		
		ServerBuilder jetty=new ServerBuilder();
		jetty.setPort(8381);
		jetty.setServerConfigurationFile("src/main/resources/jetty.xml");
		
		appBuilder.configure();
		jetty.listen(appBuilder.getContext());
	}
	
	private String webappConfigurationFile="src/main/config/jetty-env.xml";
	private String serverConfigurationFile="src/main/config/jetty.xml";
	private String jdbcDriver="com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";
	private String jdbcUrl="jdbc:mysql://localhost:3306/placement";
	private String jdbcUser="placement";
	private String jdbcPassword="placementPass";
	private int httpPort=8192;
	private String contextPath="/placement";
	private String webAppHome="../placement-rest-api/target/placement-rest-api-0.0.1-SNAPSHOT";	
		
	/**
	 * Metodo para leer un fichero de configuración para el PlacementJettyServer.
	 * 
	 */
	public void readResourceBundle() {
		PropertyResourceBundle prb=null;
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("placement.properties");
			prb=new PropertyResourceBundle(new InputStreamReader(is));
			setServerConfigurationFile(prb.getString("placement.jetty.server.configurationFile"));
			setWebappConfigurationFile(prb.getString("placement.jetty.war.configurationFile"));
			setJdbcDriver(prb.getString("placement.jdbc.driver"));
			setJdbcUrl(prb.getString("placement.jdbc.url"));
			setJdbcUser(prb.getString("placement.jdbc.user"));
			setJdbcPassword(prb.getString("placement.jdbc.password"));
			setHttpPort(Integer.parseInt(prb.getString("placement.jdbc.httpPort")));
			setContextPath(prb.getString("placement.context.path"));
			setWebAppHome(prb.getString("placement.context.webAppHome"));			
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING,"Configuration file Not Found. Doing with default!!");
		} catch (NullPointerException e) {
			log.log(Level.WARNING,"Configuration file Not Found. Doing with default!!");			
		} catch (IOException e) {
			log.log(Level.WARNING,"Can't read Configuration file.");
		}	
	}
	
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setWebappConfigurationFile(String webappConfigurationFile) {
		this.webappConfigurationFile = webappConfigurationFile;
	}

	public String getWebappConfigurationFile() {
		return webappConfigurationFile;
	}

	public void setServerConfigurationFile(String serverConfigurationFile) {
		this.serverConfigurationFile = serverConfigurationFile;
	}

	public String getServerConfigurationFile() {
		return serverConfigurationFile;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	/**
	 * Sets the Application root directory.
	 * 
	 * @return
	 */
	public void setWebAppHome(String webAppHome) {
		this.webAppHome = webAppHome;
	}

	/**
	 * Gets the Application root directory.
	 * 
	 * @return
	 */
	public String getWebAppHome() {
		return webAppHome;
	}

	/**
	 * Constructor por defecto.
	 */
	public PlacementJettyServer() {	
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String [] args) throws Exception {
		PlacementJettyServer js=new PlacementJettyServer();
		
		js.readResourceBundle();			 
		
		WebAppBuilder appBuilder=new WebAppBuilder();
		appBuilder.setResourceBase(js.getWebAppHome());
		appBuilder.setContextPath(js.getContextPath());
		appBuilder.setContextConfigFile(js.getWebappConfigurationFile());
		
		ServerBuilder jetty=new ServerBuilder();
		jetty.setPort(js.getHttpPort());
		jetty.setServerConfigurationFile(js.getServerConfigurationFile());
		
		appBuilder.configure();		
		jetty.listen(appBuilder.getContext());
	}
}
