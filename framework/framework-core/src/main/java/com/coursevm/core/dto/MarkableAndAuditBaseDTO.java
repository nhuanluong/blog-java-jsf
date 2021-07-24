package com.coursevm.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MarkableAndAuditBaseDTO extends MarkableBaseDTO {

    private Integer version;

    private Date createdDate;

    private Long createdBy;

    private Date modifiedDate;

    private Long modifiedBy;
}
