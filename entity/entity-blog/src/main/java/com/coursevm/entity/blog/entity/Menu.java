/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import com.coursevm.core.component.TermTree;
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
@DiscriminatorValue("nav_menu")
@NoArgsConstructor
public class Menu extends Term implements TermTree<Menu> {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryParentId")
	private Menu categoryParent;

	@OneToMany(mappedBy="categoryParent", fetch = FetchType.EAGER)
	private Set<Menu> subCategories = new HashSet<>();

	@Override
	public Menu getParent() {
		return getCategoryParent();
	}

	@Override
	public Set<Menu> getChildren() {
		return getSubCategories();
	}
}
