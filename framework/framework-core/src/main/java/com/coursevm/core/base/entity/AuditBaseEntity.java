package com.coursevm.core.base.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditBaseEntity extends BaseEntity {

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
}
