/*
 * Created on 2021.07.22 (y.M.d) 07:56
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.dto.MediaDTO;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.service.MediaService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.request.PageableRequest;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.core.dto.response.PagedResult;
import com.coursevm.entity.blog.entity.Media;
import com.coursevm.entity.blog.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author Nhuan Luong
 */
@Component
public class MediaRestService {

    @Autowired
    private MediaService mediaService;

    public PagedResult<MediaDTO> findAll(PageableRequest<MediaDTO> pageable) {
        return PagedResult.of(mediaService.findAll(pageable.getPageable()), MediaDTO.class);
    }

    public ObjectResult<MediaDTO> save(ObjectRequest<MediaDTO> data) {
        return ObjectResult.of(mediaService.save(data.getParam(Media.class)), MediaDTO.class);
    }

    public ObjectResult<Boolean> delete(Long id) {
        mediaService.delete(id);
        return ObjectResult.of(true);
    }

    public ObjectResult<MediaDTO> findById(Long id) {
        return ObjectResult.of(mediaService.findById(id).get(), MediaDTO.class);
    }
}
