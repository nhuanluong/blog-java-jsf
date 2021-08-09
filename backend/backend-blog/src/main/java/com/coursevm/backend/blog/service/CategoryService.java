/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.backend.blog.util.SlugUtil;
import com.coursevm.backend.blog.util.UrlFriendly;
import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.core.base.entity.MarkUpdated;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.entity.blog.entity.Category;
import com.coursevm.entity.blog.entity.QCategory;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
interface CategoryDAO extends BaseDAO<Category, Long> {
}

@Service
@Transactional
public class CategoryService extends AbstractBaseService<Category, Long> {

    @Autowired
    private CategoryDAO categoryDAO;

    private static QCategory qCategory = QCategory.category;

    @Override
    public CategoryDAO getRepository() {
        return categoryDAO;
    }

    public Category save(Category entity) {
        if (entity != null) {
            NodeType nodeType = entity;
            String slug = nodeType.getSlug();
            if (StringUtils.isBlank(slug)) {
                UrlFriendly<Category> methodCheck = new CategoryFriendLy();
                nodeType.setSlug(SlugUtil.makeSlug(nodeType.getId(), nodeType.getName(), methodCheck));
            }
        }

        if (entity instanceof MarkUpdated) {
            MarkUpdated n = (MarkUpdated) entity;
            n.setLastUpdated(LocalDateTime.now());
        }
        return categoryDAO.save(entity);
    }

    public Long delete(Long id) {

        Optional<Category> isCategory = categoryDAO.findById(id);

        if (isCategory.isEmpty()) return null;

        Category category = isCategory.get();
        Category categoryParent = category.getCategoryParent();
        Set<Category> lstChild = category.getSubCategories();

        if (CollectionUtils.isNotEmpty(lstChild)) {
            lstChild.forEach(item -> {
                item.setCategoryParent(categoryParent);
            });
            categoryDAO.saveAll(lstChild);
        }
        categoryDAO.deleteById(id);

        return id;
    }

    public boolean isExistsSlug(String slugFriendly, Long categoryId) {
        return new CategoryFriendLy().makeSlug(categoryId, slugFriendly) != null;
    }

    public String createSlug(Long categoryId, String name) {
        return SlugUtil.makeSlug(categoryId, name, new CategoryFriendLy());
    }

    public void updateCount(Long categoryId, long count) {
        Category category = findOne(categoryId);
        if (category != null) {
            category.setCategoryCount(count);
            categoryDAO.save(category);
        }
    }

    class CategoryFriendLy implements UrlFriendly<Category> {
        @Override
        public Category makeSlug(Long categoryId, String slugFriendly) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qCategory.categorySlug.containsIgnoreCase(slugFriendly));
            if (categoryId != null && categoryId > 0) {
                builder.and(qCategory.categoryId.ne(categoryId));
            }
            return getQuery().selectFrom(qCategory).where(builder).fetchFirst();
        }
    }
}
