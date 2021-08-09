/*
 * Created on 2021.07.22 (y.M.d) 07:56
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.dto.PostRequestDTO;
import com.coursevm.backend.blog.service.PostService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.request.PageableRequest;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.core.dto.response.PagedResult;
import com.coursevm.entity.blog.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nhuan Luong
 */
@Component
public class PostRestService {

    @Autowired
    private PostService postService;

    public PagedResult<PostDTO> findAll(PageableRequest<PostRequestDTO> pageable) {
        return PagedResult.of(postService.findAll(pageable.getParam(), pageable.getPageable()), PostDTO.class);
    }

    public ObjectResult<PostDTO> findById(Long id) {
        return ObjectResult.of(postService.findById(id).orElse(null), PostDTO.class);
    }

    public ObjectResult<PostDTO> save(ObjectRequest<PostDTO> data) {
        return ObjectResult.of(postService.save(data.getParam(Post.class)), PostDTO.class);
    }

    public ObjectResult<Boolean> delete(Long id) {
        postService.delete(id);
        return ObjectResult.of(true);
    }

    public String makeSlug(Long id, String name) {
        return postService.createSlug(id, name);
    }
}
