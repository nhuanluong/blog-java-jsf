package com.coursevm.core.dto;

import com.coursevm.core.base.entity.BaseId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseDTO implements BaseId, Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
    private Long tenantId;
    private Long tenantOrgId;

    @Override
    public boolean equals(Object other) {
        return (other instanceof BaseDTO) && (id != null)
                ? id.equals(((BaseDTO) other).id)
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }
}
