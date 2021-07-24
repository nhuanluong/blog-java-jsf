/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.dto.TagDTO;
import com.coursevm.backend.blog.service.TagService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.response.ListResult;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.entity.blog.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Nhuan Luong
 */
@Service
public class TagRestService {

    @Autowired
    private TagService tagService;


    public ListResult<TagDTO> findAll() {
        return ListResult.of(tagService.findAll(), TagDTO.class);
    }

    public boolean isExistsSlug(String slugFriendly, Long tagId) {
        return tagService.isExistsSlug(slugFriendly, tagId);
    }

    public ObjectResult<TagDTO> save(ObjectRequest<TagDTO> entity) {
        return ObjectResult.of(tagService.save(entity.getParam(Tag.class)), TagDTO.class);
    }

    public String makeSlug(Long categoryId, String termName) {
        return tagService.makeSlug(categoryId, termName);
    }


    public ListResult<TagDTO> complete(String name, int limit) {
        return ListResult.of(tagService.complete(name, limit), TagDTO.class);
    }

    public Long delete(Long id) {
        return tagService.delete(id);
    }

    public ObjectResult<TagDTO> findByName(String tag) {
        return ObjectResult.of(tagService.findByName(tag), TagDTO.class);
    }
}
