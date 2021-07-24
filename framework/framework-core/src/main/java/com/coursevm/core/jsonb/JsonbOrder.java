package com.coursevm.core.jsonb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JsonbOrder {

    private String direction;

    private String property;

    public JsonbOrder(String direction, String property) {
        this.direction = direction;
        this.property = property;
    }
}