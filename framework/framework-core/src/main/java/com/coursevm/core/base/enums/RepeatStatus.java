package com.coursevm.core.base.enums;

import java.util.ResourceBundle;

public enum RepeatStatus {
    ISREPEAT(1), UNREPEAT(0);

    private int key;
    private String value;

    RepeatStatus(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        this.value = ResourceBundle.getBundle("frameworkMessages").getString(String.format("%s.%s", this.getClass().getName(), this.name()));
        return this.value;
    }
}
