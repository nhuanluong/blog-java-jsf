/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.repository.CommandRepository;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.entity.blog.entity.Media;
import com.coursevm.entity.blog.entity.QMedia;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Nhuan Luong
 */
interface MediaDAO extends BaseDAO<Media, Long> {

}
@Service
@Transactional
public class MediaService extends AbstractBaseService<Media, Long> {

    @Autowired
    private MediaDAO mediaDAO;

    @Override
    public CommandRepository<Media, Long> getRepository() {
        return mediaDAO;
    }

    static QMedia qPost = QMedia.media;

    @Override
    public Media save(Media entity) {
        if (StringUtils.isBlank(entity.getSlug())) {
            entity.setSlug(makeSlug(entity.getPostId(), entity.getPostTitle()));
        }
        return super.save(entity);
    }

    public Optional<Media> checkSlugNotById(String slug, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPost.postSlug.equalsIgnoreCase(slug));
        if (id != null && id > 0) {
            builder.and(qPost.postId.ne(id));
        }
        Media value = getQuery().selectFrom(qPost).where(builder).fetchFirst();

        return Optional.ofNullable(value);
    }

    public String makeSlug(Long id, String name) {

        if (StringUtils.isBlank(name)) return name;

        name = TextUtil.makeUrlFriendly(name);

        return makeSlug(id, name, 0, name.length());
    }

    private String makeSlug(Long categoryId, String slugFriendly, int count, int length) {

        Optional<Media> post = checkSlugNotById(slugFriendly, categoryId);

        if (post.isEmpty()) return slugFriendly;

        String slug = slugFriendly.substring(0, length) + "-" + (++count);

        slug = makeSlug(categoryId, slug, count, length);

        return slug;
    }
}
