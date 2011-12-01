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

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.claudia.ClothoTest;
import com.telefonica.claudia.smi.context.impl.EnvironmentCreatorImpl;

/**
 * Unit tests for EnvironmentCreator
 * 
 * @author luismarcos.ayllon
 *
 */
public class TestEnvironmentCreatorCorrect {
    
	protected final static String VALID_IOP_FILE="valid_iop.xml";
	protected final static String IOP_MACROS_FILE="ovf_dinamic.xml";
	protected final static String EXPECTED_CONTEXT = "<?xml version='1.0' encoding='UTF-8'?>" +
		"<ns1:Environment xmlns:ns1=\"http://schemas.dmtf.org/ovf/environment/1\" ns1:id=\"100102-001\">" +
		"<ns1:PropertySection>" +
		"<ns1:Property ns1:key=\"hostname\" ns1:value=\"gandalf\"/>" +
		"<ns1:Property ns1:key=\"admin.pw\" ns1:value=\"adminadmin\"/>" +
		"</ns1:PropertySection>" +
		"</ns1:Environment>";
	protected final static String EXPECTED_PROPERTY_KPI_QUALIFIER =
		"<ns1:Property ns1:key=\"com.sun.master.KPIQualifier\" ns1:value=\"es.tid.customers.lm.services.ss1\"/>";
	protected final static String EXPECTED_PROPERTY_KPI_NAME =
		"<ns1:Property ns1:key=\"com.sun.master.KPIName\" ns1:value=\"es.tid.customers.lm.services.ss1.kpis.queueLength\"/>";
	protected final static String EXPECTED_PROPERTY_KPI_TIMES =
		"<ns1:Property ns1:key=\"com.sun.master.KPITimesUrl\" ns1:value=\"http://10.95.240.4:5555/times\"/>";
	protected final static String EXPECTED_PROPERTY_MONITORING_CHANNEL =
		"<ns1:Property ns1:key=\"com.sun.master.KPIChannel\" ns1:value=\"http://localhost:9999/monitoring/channel/999\"/>";
	protected final static String EXPECTED_PROPERTY_HOSTNAME =
		"<ns1:Property ns1:key=\"com.sun.master.Hostname\" ns1:value=\"loadBalancer1\"/>";
	
	private EnvironmentCreator myCreator;		
	private String iopContent; 
	private String iopWithMacros;

	@Before
    public void setup() throws Exception {
		myCreator = new EnvironmentCreatorImpl();
		iopContent = ClothoTest.readFile(getClass().getClassLoader().getResourceAsStream(VALID_IOP_FILE));
		iopWithMacros = ClothoTest.readFile(getClass().getClassLoader().getResourceAsStream(IOP_MACROS_FILE));
	}
    
	@Test
    public void shouldTranslateVMInfoIntoTemplate() throws Exception {
		Environment res = myCreator.create(iopContent);

		assertEquals(res.getContent(), EXPECTED_CONTEXT);
    }
	
	@Test
    public void shouldReplaceAllTheMacros() throws Exception {
		Environment res = myCreator.create(iopWithMacros);

		assertThat(res.getContent(), Matchers.containsString(EXPECTED_PROPERTY_KPI_QUALIFIER));
		assertThat(res.getContent(), Matchers.containsString(EXPECTED_PROPERTY_KPI_NAME));
		assertThat(res.getContent(), Matchers.containsString(EXPECTED_PROPERTY_KPI_TIMES));
		assertThat(res.getContent(), Matchers.containsString(EXPECTED_PROPERTY_MONITORING_CHANNEL));
		assertThat(res.getContent(), Matchers.containsString(EXPECTED_PROPERTY_HOSTNAME));
    }
}
