package com.coursevm.core.base.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class MarkableBaseTenantEntity extends BaseTenantEntity implements BaseDelete {

    @Column(name = "isDeleted")
    private Boolean isDeleted;
}
