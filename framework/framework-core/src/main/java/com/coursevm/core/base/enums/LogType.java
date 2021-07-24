package com.coursevm.core.base.enums;

public enum LogType {
	
	FATAL("FATAL"),
	ERROR("ERROR"),
	WARNING("WARNING"),
	INFO("INFO"),
	DEBUG("DEBUG");	

	private String key;
	private String value;

	private LogType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
}
