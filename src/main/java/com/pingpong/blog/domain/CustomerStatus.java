package com.pingpong.blog.domain;

import java.util.Random;

public enum CustomerStatus {
    QUALIFIED,
    UNQUALIFIED,
    NEGOTIATION,
    NEW,
    RENEWAL,
    PROPOSAL;

    public static CustomerStatus random() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}