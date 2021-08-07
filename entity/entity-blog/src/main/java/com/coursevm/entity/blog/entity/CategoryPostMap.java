/*
 * Created on 2021.08.07 (y.M.d) 10:09
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
@Entity
@Table(name = BlogSchema.categoryPostMap, catalog = BlogSchema.name)
public class CategoryPostMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryPostId;

    @Column
    private Long categoryId;

    @Column
    private Long postId;

    @Override
    public Long getId() {
        return categoryPostId;
    }
}
