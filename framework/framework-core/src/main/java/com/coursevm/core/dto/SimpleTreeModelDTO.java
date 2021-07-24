package com.coursevm.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleTreeModelDTO extends BaseDTO {
    private String name;
    private BaseDTO parent;
}
