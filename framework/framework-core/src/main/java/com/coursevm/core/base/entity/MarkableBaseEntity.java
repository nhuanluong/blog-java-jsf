package com.coursevm.core.base.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public class MarkableBaseEntity extends BaseEntity implements BaseDelete, Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "isDeleted")
    private Boolean isDeleted;
}
