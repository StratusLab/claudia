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
import java.util.HashMap;
import com.telefonica.claudia.smi.TCloudConstants.ErrorType;


public class SMIChannelEvent extends Event implements Serializable {

	private static final long serialVersionUID = 8133331375710757334L;

    private HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
    
    /**
     * Kinds of actions the SMI can request the SM to fullfill:
     * 
     * - Deploy (OVFDocument, CustomerName): deploy the service described in the OVF Document passed
     *   as a parameter, as a Service of the CustomerName.
     * 
     * - Undeploy (FQN_ID): undeploy the service indicated with the FQN.
     *   
     * @author daniel
     *
     */
    public enum SMIAction {
    	DEPLOY, 
    	UNDEPLOY,
    	GET_VAPP,
    	GET_VDC,
    	GET_VAPP_LIST,
    	DEPLOY_VDC,
    	GET_VEE,
    	GET_ORG,
    	DELETE_VDC,
    	DELETE_VEE,
    	DEPLOY_VEE,
    	POWER_VM, 
    	GET_TASK,
    	GET_MEASURE_DESCRIPTOR,
    	GET_MEASURE_DESCRIPTOR_LIST,
    	GET_MEASURE_VALUE,
    	MODIFY_HARDWARE,
    	MODIFY_VDC,
    	DELETE_SNAPSHOT,
    	TAKE_SNAPSHOT,
    	RESTORE_SNAPSHOT,
    	GET_SNAPSHOT,
    	GET_SNAPSHOT_LIST,
    	CLONE_VM
    }
    
    public static final String OVF_DOCUMENT= "OvfDocument";
    public static final String CUSTOMER_NAME= "CustomerName";
	public static final String VEE_NAME = "veeName";    
    public static final String SERVICE_NAME = "ServiceName";
    public static final String SERVICE_DESCRIPTION = "ServiceDescription";
    public static final String VEE_DESCRIPTION = "VeeDescription";
    public static final String VDC_DESCRIPTION = "VDCDescription";
    public static final String ORG_DESCRIPTION = "OrgDescription";
    public static final String FQN_ID = "fqnId";
    public static final String POWER_ACTION = "powerAction";
	public static final String TASK_ID = "taskId";
	public static final String TASK_STATUS = "taskStatus"; 
	public static final String TASK_MESSAGE = "taskMessage";
	public static final String SEQUENCE_NUMBER = "seqNumber";
	public static final String HARDWARE_ITEM = "hardwareItem";
	
	public static final String SNAPSHOT_NAME = "snapshotName";
	public static final String SNAPSHOT_DESCRIPTION = "snapshotDescription";
	public static final String SNAPSHOT_LIST = "snapshotList";
	public static final String SNAPSHOT = "snapshot";

	public static final String MEASURE_NAMES_LIST="MeasureNamesList";
	public static final String MEASURE_TYPE_ID="MeasurementTypeId";
	public static final String MEASURE_DESCRIPTION="MeasurementDescription";
	public static final String MEASURE_NAME="MeasurementName";
	public static final String MEASURE_VALUE_TYPE="MeasurementValueType";
	public static final String MEASURE_MIN_VALUE="MeasurementMinValue";
	public static final String MEASURE_MAX_VALUE="MeasurementMaxValue";
	public static final String MEASURE_FILTER_FROM="MeasurementFilterFrom";
	public static final String MEASURE_FILTER_TO="MeasurementFilterTo";
	public static final String MEASURE_FILTER_NUMBER="MeasurementFilterNumber";
	public static final String MEASURE_FILTER_INTERVAL="MeasurementFilterInterval";
	
	public static final String MEASURE_VALUES="MeasurementValues";
	public static final String MEASURE_UNITS="MeasurementUnits";

	public static final String CUSTOMER_DESCRIPTION =  "CustomerDescription";
    
    private SMIAction action;
    
    private boolean success=false;

	private String message;
	
    private ErrorType errorType;
	
	public SMIChannelEvent(long t_0, long deltaT, SMIAction action) {
		super(t_0, deltaT, EventType.SMI_CHANNEL_EVENT);
		
		this.action = action;
	}
    
	/**
	 * Each SMIAction will have a variable number of parameters. Those parameters will be
	 * encoded in an internal map, that the LCC will access in order to fullfill the request.
	 * 
	 * @param parameterName
	 * @param value
	 */
	public void put(String parameterName, String value) {
		parameters.put(parameterName, value);
	}
	
	public String get(String parameterName) {
		return (String) parameters.get(parameterName);
	}
	
	public void putSerializable(String parameterName, Serializable value) {
		parameters.put(parameterName, value);
	}
	
	public Serializable getSerializable(String parameterName) {
		return parameters.get(parameterName);
	}
	
    public SMIAction getAction() {
    	return action;
    }
	
    @Override
    public String toString() {
        return super.toString();
    }

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
	
    public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}	
}
