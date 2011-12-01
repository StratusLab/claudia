/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.template;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.claudia.ClothoTest;

/**
 * Unit tests for OneTemplateUtils
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestTemplateUtilsCorrect {
    
    
	protected final static String VALID_IOP_FILE="valid_iop.xml";
	protected final static String FAKED_FQN="fakedOrg.customers.fakedVDC.services.FakedVapp.vees.FakedVee";
	
	protected final static String EXPECTED_NAME = "NAME=fakedOrg.customers.fakedVDC.services.FakedVapp.vees.FakedVee.replicas.1";
	protected final static String EXPECTED_MIGRABILITY =	"MIGRABILITY=NONE";
	protected final static String EXPECTED_OS = "OS=[boot=hd,";
	protected final static String EXPECTED_ROOT =	"root=hda1]";
	protected final static String EXPECTED_CONTEXT = "CONTEXT=[CustomizationUrl=\"http://84.21.173.28:18888/fakedOrg.customers.fakedVDC.services.FakedVapp.vees.FakedVee.replicas.1\",";
	protected final static String EXPECTED_FILES = "files=\"/env/path//fakedOrg.customers.fakedVDC.services.FakedVapp.vees.FakedVee.replicas.1/ovf-env.xml\",";
	protected final static String EXPECTED_TARGET = "target=\"hdc\"]";
	protected final static String EXPECTED_CPU = "CPU=1.0";
	protected final static String EXPECTED_VCPU = "VCPU=1.0";
	protected final static String EXPECTED_MEMORY = "MEMORY=1.0";
	protected final static String EXPECTED_DISK =	"DISK=[source=http://84.21.173.55:81/disk.img,target=hda,size=50,digest=null]";
	protected final static String EXPECTED_NIC =	"NIC=[network=fakedOrg.customers.fakedVDC.services.FakedVapp.networks.sge_net,ip=null]";
	protected final static String EXPECTED_RAW =	"RAW = [ type =\"kvm\", data =\"<devices><serial type='pty'><source path='/dev/pts/5'/><target port='0'/></serial><console type='pty' tty='/dev/pts/5'><source path='/dev/pts/5'/><target port='0'/></console></devices>\" ]";

	private String iopContent; 

	@Before
    public void setup() throws Exception {
		iopContent = ClothoTest.readFile(getClass().getClassLoader().getResourceAsStream(VALID_IOP_FILE));
	}
    
	@Test
    public void shouldTranslateVMInfoIntoTemplate() throws Exception {
		String res = OneTemplateUtils.TCloud2ONEVM(iopContent, FAKED_FQN);
		
		assertThat(res, Matchers.containsString(EXPECTED_NAME));
		assertThat(res, Matchers.containsString(EXPECTED_MIGRABILITY));
		assertThat(res, Matchers.containsString(EXPECTED_OS));
		assertThat(res, Matchers.containsString(EXPECTED_ROOT));
		assertThat(res, Matchers.containsString(EXPECTED_CONTEXT));
		assertThat(res, Matchers.containsString(EXPECTED_FILES));
		assertThat(res, Matchers.containsString(EXPECTED_TARGET));
		assertThat(res, Matchers.containsString(EXPECTED_CPU));
		assertThat(res, Matchers.containsString(EXPECTED_VCPU));
		assertThat(res, Matchers.containsString(EXPECTED_MEMORY));
		assertThat(res, Matchers.containsString(EXPECTED_DISK));
		assertThat(res, Matchers.containsString(EXPECTED_NIC));
		assertThat(res, Matchers.containsString(EXPECTED_RAW));
    }
}
