/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.context;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.claudia.ClothoTest;
import com.telefonica.claudia.clotho.utils.PropertyManager;
import com.telefonica.claudia.smi.context.impl.EnvironmentImpl;
import com.telefonica.claudia.smi.context.impl.EnvironmentLocalDeployerImpl;
import com.telefonica.claudia.smi.utils.Constants;
import com.telefonica.claudia.smi.utils.OneProperties;

/**
 * Unit tests for EnvironmentDeployer
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestEnvironmentDeployerCorrect {
    
    
	protected final static String VALID_IOP_FILE = "valid_iop.xml";
	protected final static String FAKED_VMFQN = "vm.id";
	protected final static String FAKED_CONTEXT = "<?xml version='1.0' encoding='UTF-8'?>" +
		"<ns1:Environment xmlns:ns1=\"http://schemas.dmtf.org/ovf/environment/1\" ns1:id=\"100102-001\">" +
		"<ns1:PropertySection>" +
		"<ns1:Property ns1:key=\"fakedKey\" ns1:value=\"fakedValue\"/>" +
		"</ns1:PropertySection>" +
		"</ns1:Environment>";
	protected final static String EXPECTED_FILE_PATH = PropertyManager.getInstance().getProperty(OneProperties.PATH_TO_CONTEXT_REPOSITORY) + "/" + FAKED_VMFQN + "/" + Constants.ENVIRONMENT_FILE_NAME;

	private EnvironmentDeployer myDeployer;		
	private EnvironmentImpl env;

	@Before
    public void setup() throws Exception {
		myDeployer = new EnvironmentLocalDeployerImpl();
		
		env = new EnvironmentImpl();
		env.setVmFqn(FAKED_VMFQN);
		env.setContent(FAKED_CONTEXT);
	}
    
	@Test
    public void shouldDeployTheEnvironmentInLocal() throws Exception {
		myDeployer.deploy(env);
		
		File expectedFile = new File (EXPECTED_FILE_PATH);
		
		assertTrue(expectedFile.exists());
		String contextContent = ClothoTest.readFile(new FileInputStream(expectedFile));
		assertThat(contextContent, Matchers.containsString(FAKED_CONTEXT));
    }
	
	@After
	public void restore () throws Exception {
		File file = new File (EXPECTED_FILE_PATH);
		file.delete();
		
		File subdir = new File (PropertyManager.getInstance().getProperty(OneProperties.PATH_TO_CONTEXT_REPOSITORY) + "/" + FAKED_VMFQN);
		subdir.delete();
		
		File dir = new File (PropertyManager.getInstance().getProperty(OneProperties.PATH_TO_CONTEXT_REPOSITORY));
		dir.delete();
	}  
}
