package com.coursevm.core.base.enums;

import java.util.ResourceBundle;

public enum DataConfigType {
    NULL(0),
    DATASOURCE(1),
    CONFIG(2);

    private int key;
    private String value;

    DataConfigType(int key) {
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
