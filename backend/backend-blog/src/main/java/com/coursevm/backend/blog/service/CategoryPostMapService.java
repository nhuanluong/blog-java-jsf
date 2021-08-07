/*
 * Created on 2021.08.07 (y.M.d) 10:45
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.backend.blog.component.ObserverPost;
import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.core.backend.util.DataSourceContext;
import com.coursevm.entity.blog.entity.QCategoryPostMap;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
@Service
@Transactional
public class CategoryPostMapService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryService categoryService;

    static QCategoryPostMap qCategoryPostMap = QCategoryPostMap.categoryPostMap;

    public void updateCount(ObserverPost event) {

        Set<CategoryDTO> categories = new HashSet<>();

        Optional<PostDTO> post = event.getPost();
        if (post.isPresent() && CollectionUtils.isNotEmpty(post.get().getCategories())) {
            categories.addAll(post.get().getCategories());
        }

        Optional<PostDTO> postOld = event.getPostOld();
        if (postOld.isPresent() && (CollectionUtils.isNotEmpty(postOld.get().getCategories()))) {
            categories.addAll(postOld.get().getCategories());
        }

        if (CollectionUtils.isEmpty(categories)) return;

        categories.forEach(item -> {
            JPAQuery<Long> query = new JPAQueryFactory(entityManager)
                    .select(qCategoryPostMap.postId)
                    .from(qCategoryPostMap)
                    .where(qCategoryPostMap.categoryId.eq(item.getCategoryId()));
            categoryService.updateCount(item.getCategoryId(), query.fetchCount());
        });
    }
}
