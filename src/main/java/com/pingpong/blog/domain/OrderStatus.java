package com.pingpong.blog.domain;

import java.util.Random;

public enum OrderStatus {
    PENDING,
    DELIVERED,
    CANCELLED,
    RETURNED;

    public static OrderStatus random() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}