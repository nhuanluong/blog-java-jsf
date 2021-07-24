package com.coursevm.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkableBaseDTO extends BaseDTO {
    private Boolean isDeleted;
}
