package com.coursevm.core.base.entity;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public class BaseTenantEntity implements BaseId {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long tenantId;

    @Transient
    private Long tenantOrgId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseTenantEntity)) return false;
        BaseTenantEntity that = (BaseTenantEntity) o;
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
