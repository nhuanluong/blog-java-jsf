package com.coursevm.core.base.enums;

import java.util.ResourceBundle;

public enum RecordStatus {
	ACTIVE(true), UNACTIVE(false);

	private Boolean key;
	private String value;

	private RecordStatus(Boolean key) {
		this.key = key;
	}

	public Boolean getKey() {
		return this.key;
	}

	public String getValue() {
		this.value = ResourceBundle.getBundle("frameworkMessages").getString(String.format("%s.%s", this.getClass().getName(), this.name()));
		return this.value;
	}
}
