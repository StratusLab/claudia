package com.telefonica.claudia.configmanager.common;

public final class ReturnCode {
	// SUCCESS CODES >= 0
	public static final int SUCCESS_NODE_ADD = 0;
	public static final int SUCCESS_NODE_IP_UPDATE = 1;
	public static final int SUCCESS_NODE_DELETE = 2;
	public static final int SUCCESS_CONFIG_UPDATE = 3;
	
	// ERROR CODES < 0
	public static final int ERROR_NODE_ADD = -1;
	public static final int ERROR_EXISTING_FQN = -2;
	public static final int ERROR_NOT_AVAILABLE_NODE = -3;
	public static final int ERROR_CLIENT_INVALID_REQUEST = -4;
	public static final int ERROR_INTERNAL_SERVER_ERROR = -5;
	
	// EXCEPTIONS
	public static final int EX_FILE_NOT_EXISTS = 4000;
	public static final int EX_NOT_VALID_FILE = 4001;
	public static final int EX_CANT_READ_FILE = 4002;
	public static final int EX_CANT_WRITE_FILE = 4003;
	public static final int EX_CANT_CREATE_BACKUP = 4004;
	public static final int EX_DIR_NOT_EXISTS = 4010;
	public static final int EX_NOT_VALID_DIR = 4011;
	public static final int EX_CANT_READ_DIR = 4012;
	public static final int EX_CANT_WRITE_DIR = 4013;
	
	private ReturnCode() {}
}
