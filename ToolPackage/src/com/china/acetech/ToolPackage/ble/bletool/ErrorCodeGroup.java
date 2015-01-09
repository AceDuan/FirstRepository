package com.china.acetech.ToolPackage.ble.bletool;

public class ErrorCodeGroup {

	public static final ErrorCode WEB_SYNC_FAIL = ErrorCode.FLAG1;
	public static final ErrorCode MATCHKEY_WRONG = ErrorCode.FLAG2;
	
	
	public enum ErrorCode{
		NULL, FLAG1, FLAG2
	}
}
