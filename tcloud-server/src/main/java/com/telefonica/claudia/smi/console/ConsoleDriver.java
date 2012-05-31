/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Righ
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.console;

import java.io.IOException;

public interface ConsoleDriver {
	/**
	 * Adquires a ticket
	 * @return Ticket id
	 * @throws IOException
	 */
	public String getTicket() throws IOException;
}
