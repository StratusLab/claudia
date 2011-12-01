/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.exception;

/**
 * Thrown when the XML content can not be parsed
 * 
 * @author luismarcos.ayllon
 *
 */
public class ParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParserException() {
		super();
	}

	public ParserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ParserException(String arg0) {
		super(arg0);
	}

	public ParserException(Throwable arg0) {
		super(arg0);
	}

}
