/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.common;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.telefonica.claudia.slm.eventsBus.AdministrationConnector;
import com.telefonica.claudia.slm.eventsBus.events.AdministrativeEvent;
import com.telefonica.claudia.slm.eventsBus.events.AdministrativeEvent.CommandType;
import com.telefonica.claudia.slm.eventsBus.events.AdministrativeEvent.SubcommandType;
import com.telefonica.claudia.slm.lifecyclemanager.LifecycleController;


public class ServiceManagerServer implements Observer {
	
	private static final int ERROR_CODE_BAD_PARAMETERS=1;	
	private static final int ERROR_CODE_SYSTEM_CONFIGURATION=2;
	private static final int ERROR_CODE_COMMAND_EXECUTION=3;
	
	private static final int POOLING_DELAY=20000;
	
	private static Logger logger = Logger.getLogger(ServiceManagerServer.class); 
	
	private static ServiceManagerServer sms = new ServiceManagerServer();
	
    static {
        Logger.getLogger("com.telefonica.claudia.slm.launcher.ServiceManagerServer").addAppender(
        		new ConsoleAppender(new PatternLayout("%-5p [%t] %c{2}: %m%n"),
                "System.out"));   	
    }

    //--------------------------------------------------------------------------------------------------------------------------
	private LifecycleController lcc = null;

    //--------------------------------------------------------------------------------------------------------------------------    
	private static void showUsage() {
		System.out.println("Usage:\n\tclotho <command> [<subcommand>] [<options>]\n\n");
		System.out.println("\t start");
		System.out.println("\t\tStart a new Service Manager process.\n");
		
		System.out.println("\t stop");
		System.out.println("\t\tStop a Service Manager that is currently running.\n");
		
		System.out.println("\t status");
		System.out.println("\t\tGet information on the Service Manager that is currently running.\n");
		
		System.out.println("\t networkrange create <description>");
		System.out.println("\t\tCreate a new network range with the provided description.\n");
		
		System.out.println("\t networkrange remove <network_name>");
		System.out.println("\t\tCreate the network range with the provided network name. Will fail if its in use.\n");
	}
	
    public static void main(String[] args) throws MalformedURLException, RemoteException, AlreadyBoundException, InterruptedException, JMSException, NamingException {

    	if (args.length < 1) {
    		System.out.println("Command not recognized. \n");
    		showUsage();
    		System.exit(ERROR_CODE_BAD_PARAMETERS);
    	}
    	
    	CommandType command = CommandType.START;
    	try {
    		command = AdministrativeEvent.CommandType.valueOf(args[0].toUpperCase());
    	} catch (IllegalArgumentException iae) {
    		System.out.println("Command not recognized. \n");
    		showUsage();
    		System.exit(ERROR_CODE_BAD_PARAMETERS);
    	}
    	
    	if (command != CommandType.START) {
    		LogManager.getLoggerRepository().setThreshold(Level.ERROR);
    	}
    	
    	try {
			SMConfiguration.loadProperties();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERROR_CODE_SYSTEM_CONFIGURATION);
		}
		
    	if (command != CommandType.START) {
    		try {
        		if (executeCommand(command, args))
        			System.exit(0);
        		else {
        			System.exit(ERROR_CODE_COMMAND_EXECUTION);
        		}
        		
    		} catch (JMSException je) {
    			logger.error("Exception connecting to the JMS queues: " +je.getMessage());
    		} catch (NamingException ne) {
    			logger.error("Exception in JNDI searching for connection data: " + ne.getMessage());
    		}
    	}
    	
    	logger.info("\n\n\n\n\nInitiating a new Service Lifecycle Manager Instance");
    	logger.info("--------------------------------------------------------------------------------------------------------------\n");

    	sms.lcc = new LifecycleController();
    	logger.info("Lifecycle manager created");
    	
    	// Prepare to receive the termination signal
    	try {
	    	final SignalHandler sh = new SignalHandler();
	    	sh.addObserver( sms );
	    	sh.handleSignal( "TERM" );
	    	logger.info("Signal handlers attached");
    	} catch (Throwable t) {
    		t.printStackTrace();
    		logger.warn("Watch Out!! Signal handler mechanisms could not be started, probably because a lack of support in this JavaVM. SLM can only be stopped with a kill signal.");
    	}
    	
    	logger.info("Initialization finished, waiting for requests\n\n\n\n\n");
    	
    	while (true) {
    		Thread.sleep(POOLING_DELAY);
    	}
    }
    
    /**
     * Execute a command issued from the command line. 
     * 
     * @param command
     * 		Command to be executed.
     * 
     * @param args
     * 		Arguments for the command. The first one is the textual representation of the command itself.
     * 
     * @return
     * 		true if the command has executed correctly. 
     * 		false otherwise.
     * 
     * @throws NamingException
     * @throws JMSException
     */
	private static boolean executeCommand(CommandType command, String[] args) throws NamingException, JMSException {
		
		AdministrationConnector connector = new AdministrationConnector();
		
		SubcommandType subcommand = null;
		
		if (args.length >1) 
			try {
				subcommand = SubcommandType.valueOf(args[1].toUpperCase());
			} catch (IllegalArgumentException iae) {
				System.out.println("\n\n Syntax was not recognized. Use help for command syntax.");
				
				return false;
			}
		
		switch (command) {
			case STOP:
				connector.finish();
				return true;
				
			case NETWORKRANGE:
				
				// Two actions can be done, "CREATE" and "REMOVE"
				if (args.length > 2 && subcommand == SubcommandType.CREATE) {
					connector.createNetworkRange(args[2].replace(" ", ""));
				} else if (args.length > 2 && subcommand == SubcommandType.REMOVE) {
					connector.removeNetworkRange(args[2].replace(" ", ""));
				} else {
					System.out.println("\n\n Syntax was not recognized. Use help for command syntax.");
					return false;
				}
				
				break;
				
			case STATUS:
				System.out.println("\n\n" + connector.getStatus() + "\n\n");
				return true;
				
			case HELP:
			default:
				showUsage();
				return true;
		}
		
		return false;
	}

	public void update(Observable arg0, Object arg1) {
		
		logger.info("Termination signal received. Saving actual state and asking services to finish.");
		
		if (lcc!=null)
			lcc.finish();
		
		logger.info("Returned from SLM's last stage. Exiting.");
		System.exit(0);
	}
}
