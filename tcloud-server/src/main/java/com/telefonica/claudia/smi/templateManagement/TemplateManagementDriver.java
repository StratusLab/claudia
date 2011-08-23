/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.templateManagement;

import java.io.IOException;


public interface TemplateManagementDriver {

	/**
	 * Get Template Gatalog
	 * @return Task id
	 * @throws IOException
	 */
	public String getCatalog() throws IOException;

	public String getCatalogItem(String catalogId) throws IOException;
	
	public String getTemplate(String templateId) throws IOException;

	/**
	 * Create a template from a given VM
	 * @param fqn 
	 * 		VM fqn
	 * @param templateName
	 * 		Template name that identifies this template in the inventory
	 * @return Task id
	 * @throws IOException
	 */
	public long takeTemplate(String fqnVM, String templateName) throws IOException;
	
}
