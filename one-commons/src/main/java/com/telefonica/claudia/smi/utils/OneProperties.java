/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.utils;

/**
 * The OpenNebula properties that can be found in the property file
 * 
 * @author luismarcos.ayllon
 *
 */
public class OneProperties {
	
	public final static String URL_PROPERTY = "management.provisioning.one.url";
	public final static String USER_PROPERTY = "management.provisioning.one.user";
	public final static String PASSWORD_PROPERTY = "management.provisioning.one.password";	
	
	public final static String CONTEXT_HOST_PROPERTY = "management.provisioning.one.context.host";
	public final static String CONTEXT_PORT_PROPERTY = "management.provisioning.one.context.port";
	public static final String PATH_TO_CONTEXT_REPOSITORY = "management.provisioning.one.context.repository";
	public static final String MONITORING_CHANNEL = "management.provisioning.one.context.monitoringChannel";
	
	public static final String KERNEL_PROPERTY = "management.provisioning.one.kernel";
	public static final String INITRD_PROPERTY = "management.provisioning.one.initrd";
	public static final String ENVIRONMENT_PROPERTY = "management.provisioning.one.environmentPath";
	
	public static final String KEY_PORT = "com.telefonica.claudia.server.port";
	public static final String KEY_HOST = "com.telefonica.claudia.server.host";
	public static final String CUSTOMIZATION_PORT_PROPERTY = "com.telefonica.claudia.customization.port";
	
}
