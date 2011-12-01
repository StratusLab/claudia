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
 * XMLRPC commands to access OpenNebula features
 *  
 * @author luismarcos.ayllon
 *
 */
public class OneActions {

	public final static String VM_ALLOCATION_COMMAND = "one.vm.allocate";
	public final static String VM_UPDATE_COMMAND = "one.vm.action";
	public final static String VM_GETINFO_COMMAND = "one.vm.info";
	public final static String VM_GETALL_COMMAND = "one.vmpool.info";
	public final static String VM_DELETE_COMMAND = "one.vm.delete";
	
	public final static String NET_ALLOCATION_COMMAND = "one.vn.allocate";
	public final static String NET_GETINFO_COMMAND = "one.vn.info";
	public final static String NET_GETALL_COMMAND = "one.vnpool.info";	
	public final static String NET_DELETE_COMMAND = "one.vn.delete";
	
}
