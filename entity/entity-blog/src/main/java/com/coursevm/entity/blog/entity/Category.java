/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import com.coursevm.core.component.TermTree;
import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
@Entity
@Getter
@Setter
@DiscriminatorValue("category")
@NoArgsConstructor
public class Category extends Term implements TermTree<Category> {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryParentId")
	private Category categoryParent;

	@OneToMany(mappedBy="categoryParent", fetch = FetchType.EAGER)
	private Set<Category> subCategories = new HashSet<>();

	@Override
	public Category getParent() {
		return getCategoryParent();
	}

	@Override
	public Set<Category> getChildren() {
		return getSubCategories();
	}
}
