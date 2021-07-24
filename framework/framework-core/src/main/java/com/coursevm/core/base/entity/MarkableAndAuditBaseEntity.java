package com.coursevm.core.base.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class MarkableAndAuditBaseEntity extends MarkableBaseEntity {

    @Column
    private Integer version;

    @Column
    private Date createdDate;

    @Column
    private Long createdBy;

    @Column
    private Date modifiedDate;

    @Column
    private Long modifiedBy;

    public MarkableAndAuditBaseEntity withCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MarkableAndAuditBaseEntity withCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MarkableAndAuditBaseEntity withModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public MarkableAndAuditBaseEntity withModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public MarkableAndAuditBaseEntity withVersion(Integer version) {
        this.version = version;
        return this;
    }
}
