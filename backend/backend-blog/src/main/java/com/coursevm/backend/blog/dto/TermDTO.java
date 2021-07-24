/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.dto;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
public class TermDTO extends BaseEntity implements NodeType {

    private Long categoryId;

    private String categoryType;

    private String categoryName;

    private String categorySlug;

    private Integer categoryGroup;

    private String categoryDescription;

    private Long categoryCount;

    @Override
    public Long getId() {
        return categoryId;
    }

    @Override
    public String getName() {
        return getCategoryName();
    }

    @Override
    public void setType(String type) {
        this.categoryType = type;
    }

    @Override
    public String getType() {
        return getCategoryType();
    }

    @Override
    public String getSlug() {
        return getCategorySlug();
    }

    @Override
    public void setSlug(String slug) {
        setCategorySlug(slug);
    }
}
