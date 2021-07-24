/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.dto;

import com.coursevm.core.component.TermTree;
import com.coursevm.entity.blog.entity.Term;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO extends TermDTO implements TermTree<CategoryDTO> {

	private CategoryDTO categoryParent;

	private Set<CategoryDTO> subCategories = new HashSet<>();

	/*@ManyToMany(mappedBy = "categories")
	private Set<Post> posts;*/

	@Override
	public CategoryDTO getParent() {
		return getCategoryParent();
	}

	@Override
	public Set<CategoryDTO> getChildren() {
		return getSubCategories();
	}
}
