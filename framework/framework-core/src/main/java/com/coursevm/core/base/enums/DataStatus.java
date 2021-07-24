package com.coursevm.core.base.enums;

import java.util.ResourceBundle;

public enum DataStatus {
	ACTIVE(1),
	UNACTIVE(0);

	private int key;
	private String value;

	DataStatus(int key) {
		this.key = key;
	}

	public int getKey() {
		return this.key;
	}

	public String getValue() {
		this.value = ResourceBundle.getBundle("frameworkMessages").getString(String.format("%s.%s", this.getClass().getName(), this.name()));
		return this.value;
	}
}

