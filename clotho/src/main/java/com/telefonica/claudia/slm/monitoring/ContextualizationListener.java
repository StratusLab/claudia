package com.telefonica.claudia.slm.monitoring;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.*;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.StringRepresentation;

import com.telefonica.claudia.slm.common.SMConfiguration;

public class ContextualizationListener {
	
	private static final String repositoryDir = "./repository/";
	
	private Component component = null;
    private String host = null;
    private int port = -1;
    private String contextPath = null;
    
    private boolean running = false;

    private static Logger logger = Logger.getLogger(ContextualizationListener.class);
    
    private static ContextualizationListener _instance = null;
    
    static {
    	logger.setLevel(Level.INFO);
    }

    public static ContextualizationListener getInstance() {
    	if(_instance == null)
    		_instance = new ContextualizationListener();
    	return _instance;
    }
	
	   private ContextualizationListener() {
	    	this(SMConfiguration.getInstance().getImagesServerHost(), 
	    			SMConfiguration.getInstance().getImagesServerPort(), SMConfiguration.getInstance().getImagesServerPath());
	    }
	    
	    private ContextualizationListener (String host, int port, String contextPath){
	    	
	    	if(host == null)
	    		throw new IllegalArgumentException("Inet address cannot be null");
			
			if(contextPath == null)
				throw new IllegalArgumentException("Path of rest server cannot be null");
			
			if(port <= 0)
				throw new IllegalArgumentException("Port must be a positive number");
			
			this.host = host;
			this.port = port;
			this.contextPath = contextPath;
		}
		
		public void start()  throws JMSException, NamingException {
			
				        
	        logger.info("Starting Image REST server");        
	        component = new Component();
			
	        component.getServers().add(Protocol.HTTP, host, port);
	      //  component.getContext().getAttributes().put(SM_MONITOR_CONTEXT_FOR_RESTLET_COMPONENTS, monitoringDataForwarder);
	       // component.getDefaultHost().attach(new MonitoringRestletApplication(component.getContext(), monitoringPath));        

	        try {
				SMConfiguration.loadProperties();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			 String customImagesDir = repositoryDir
			+ SMConfiguration.getInstance().getImagesServerPath();
			// TODO Auto-generated method stub
			
			// Create a new Restlet component and add a HTTP server connector to it  
			Component component = new Component();  
	//
			component.getServers().add(Protocol.HTTP,SMConfiguration.getInstance().getImagesServerPort());  
			  
			// Create a new tracing Restlet  
			Restlet restlet = new Restlet() {  
			    public void handle(Request request, Response response) {  
			        // Print the requested URI path  
			        String message = "Resource URI  : " + request.getResourceRef()  
			                + '\n' + "Root URI      : " + request.getRootRef()  
			                + '\n' + "Routed part   : "  
			                + request.getResourceRef().getBaseRef() + '\n'  
			                + "Remaining part: "  
			                + request.getResourceRef().getRemainingPart();  
			        response.setEntity(message, MediaType.TEXT_PLAIN);  
			        
			        
			        
			   
			        String readFileAsString = null;
			        // "./target/clotho-0.1.14-SNAPSHOT-environment/clotho/repository/" + request.getResourceRef().getRemainingPart()
					try {
						readFileAsString = readFileAsString (repositoryDir + request.getResourceRef().getRemainingPart());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
					try
					{
			        FileRepresentation representation = new FileRepresentation(repositoryDir + request.getResourceRef().getRemainingPart(), MediaType.TEXT_XML);

		            System.out.println("Data representation returned [" +  readFileAsString + "]");
		            response.setStatus(Status.SUCCESS_OK);
		            response.setEntity(representation);
					}
					catch (Exception e)
					{
						 response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					}
			    }  
			};  
			  
			System.out.println (SMConfiguration.getInstance().getImagesServerPath());
			// Then attach it to the local host  
			component.getDefaultHost().attach(SMConfiguration.getInstance().getImagesServerPath(), restlet);  
			  
			// Now, let's start the component!  
			// Note that the HTTP server connector is also automatically started.  
			try {
				component.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        
			running = true;

			logger.info("Image REST server listening at " + host + ":" + port);
	        
		}
		
		public void stop() {
			
			if(!running){
				logger.warn("Rest bus connector is not started, ignoring stop petition...");
				return;
			}
			
	        logger.info("Stopping REST server");		
			try {	        
				component.stop();
			} catch (Exception ex) {
				logger.error("Exception caught when trying to close Rest server", ex);
			}		
	        logger.info("REST server stopped");
			
	        logger.info("Closing bus connection");
		
	        logger.info("Bus connection closed");
	        
	        running = false;
		}
	
	private  String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
