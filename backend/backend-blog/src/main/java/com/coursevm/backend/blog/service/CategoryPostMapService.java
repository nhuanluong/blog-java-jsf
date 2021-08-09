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
import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.entity.blog.entity.CategoryPostMap;
import com.coursevm.entity.blog.entity.QCategoryPostMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
interface CategoryPostMapDAO extends BaseDAO<CategoryPostMap, Long> {}
@Service
@Transactional
public class CategoryPostMapService {

    @Autowired
    private CategoryPostMapDAO categoryPostMapDAO;

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
        if (postOld.isPresent() && CollectionUtils.isNotEmpty(postOld.get().getCategories())) {
            categories.addAll(postOld.get().getCategories());
        }

        if (CollectionUtils.isEmpty(categories)) return;

        categories.forEach(item -> {
            long count = categoryPostMapDAO.count(qCategoryPostMap.categoryId.eq(item.getCategoryId()));
            categoryService.updateCount(item.getCategoryId(), count);
        });
    }
}
