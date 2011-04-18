package com.telefonica.claudia.slm.monitoring.report.export;

public class ExportException extends Exception {
	public ExportException() {
		super();
	}
	
	public ExportException(String message) {
		super(message);
	}
	
	public ExportException(String message, Throwable t) {
		super(message, t);
	}
}
