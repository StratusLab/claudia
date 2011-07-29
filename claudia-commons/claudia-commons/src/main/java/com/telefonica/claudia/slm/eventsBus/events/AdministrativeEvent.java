/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.slm.eventsBus.events;

import java.io.Serializable;
import java.util.ArrayList;

public class AdministrativeEvent extends Event implements Serializable {

	private static final long serialVersionUID = -7682928637554701664L;

	public enum CommandType {
		START,
    	STOP, 
    	NETWORKRANGE,
    	STATUS,
    	HELP
    }
    
    public enum SubcommandType {
    	CREATE, 
    	LIST,
    	GET,
    	REMOVE,
    	NONE
    }
    
    private CommandType command;
    private SubcommandType subcommand = SubcommandType.NONE;
    
    private ArrayList<String> options = new ArrayList<String>();
    private String payload;
	
	public AdministrativeEvent(long t_0, long deltaT, CommandType com) {
		super(t_0, deltaT, EventType.ADMINISTRATIVE_EVENT);
		
		this.command = com;
	}

	public void setSubcommand(SubcommandType subcommand) {
		this.subcommand = subcommand;
	}

	public SubcommandType getSubcommand() {
		return subcommand;
	}

	public CommandType getCommand() {
		return command;
	}
	
	public void addOption(String option) {
		options.add(option);
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPayload() {
		return payload;
	}

}
