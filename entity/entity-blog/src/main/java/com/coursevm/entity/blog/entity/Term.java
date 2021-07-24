/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "categoryType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
@Table(name = BlogSchema.category, catalog = BlogSchema.name)
@Entity
public abstract class Term extends BaseEntity implements NodeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(insertable = false, updatable = false)
    private String categoryType;

    @Column
    @NotBlank
    private String categoryName;

    @Column
    private String categorySlug;

    @Column
    private Integer categoryGroup;

    @Column
    private String categoryDescription;

    @Column
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

    public void setCategoryName(String categoryName) {
        this.categoryName = StringUtils.trimToEmpty(categoryName);
    }
}
