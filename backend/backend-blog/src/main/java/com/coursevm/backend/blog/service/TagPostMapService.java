/*
 * Created on 2021.08.07 (y.M.d) 10:45
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.backend.blog.component.ObserverPost;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.dto.TagDTO;
import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.entity.blog.entity.QTagPostMap;
import com.coursevm.entity.blog.entity.TagPostMap;
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
interface TagPostMapDAO extends BaseDAO<TagPostMap, Long> {
}

@Service
@Transactional
public class TagPostMapService {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagPostMapDAO tagPostMapDAO;

    static QTagPostMap qTagPostMap = QTagPostMap.tagPostMap;

    public void updateCount(ObserverPost event) {

        Set<TagDTO> tags = new HashSet<>();

        Optional<PostDTO> post = event.getPost();
        if (post.isPresent() && CollectionUtils.isNotEmpty(post.get().getTags())) {
            tags.addAll(post.get().getTags());
        }

        Optional<PostDTO> postOld = event.getPostOld();
        if (postOld.isPresent() && CollectionUtils.isNotEmpty(postOld.get().getTags())) {
            tags.addAll(postOld.get().getTags());
        }

        if (CollectionUtils.isEmpty(tags)) return;

        tags.forEach(item -> {
            long count = tagPostMapDAO.count(qTagPostMap.tagId.eq(item.getCategoryId()));
            tagService.updateCount(item.getCategoryId(), count);
        });
    }
}
