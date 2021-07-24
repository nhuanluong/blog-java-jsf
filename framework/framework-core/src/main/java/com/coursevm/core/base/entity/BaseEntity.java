package com.coursevm.core.base.entity;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Setter
@Getter
public class BaseEntity implements BaseId, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tenantId;

    @Column
    private Long tenantOrgId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equal(getId(), that.getId()) &&
                Objects.equal(getTenantId(), that.getTenantId()) &&
                Objects.equal(getTenantOrgId(), that.getTenantOrgId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getTenantId(), getTenantOrgId());
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }
}
