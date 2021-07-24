/*
 * Created on 2021.07.12 (y.M.d) 21:19
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.entity;

import com.coursevm.core.base.entity.BaseId;
import com.google.common.base.Objects;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author Nhuan Luong
 */
@MappedSuperclass
public abstract class BaseEntity implements BaseId, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }
}
