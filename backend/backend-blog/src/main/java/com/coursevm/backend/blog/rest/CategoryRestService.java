/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.service.CategoryService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.response.ListResult;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.entity.blog.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nhuan Luong
 */
@Component
public class CategoryRestService {

    @Autowired
    private CategoryService categoryService;

    public ObjectResult<CategoryDTO> save(ObjectRequest<CategoryDTO> entity) {
        return ObjectResult.of(categoryService.save(entity.getParam(Category.class)), CategoryDTO.class);
    }

    public Long delete(Long id) {
        return categoryService.delete(id);
    }

    public String makeSlug(Long categoryId, String name) {
        return categoryService.createSlug(categoryId, name);
    }

    public ListResult<CategoryDTO> findAll() {
        return ListResult.of(categoryService.findAll(), CategoryDTO.class);
    }

    public ObjectResult<CategoryDTO> findOne(Long id) {
        return ObjectResult.of(categoryService.findOne(id), CategoryDTO.class);
    }

    public boolean isExistsSlug(String slugFriendly, Long categoryId) {
        return categoryService.isExistsSlug(slugFriendly, categoryId);
    }
}
