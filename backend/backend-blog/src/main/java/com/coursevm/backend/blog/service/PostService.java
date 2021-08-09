/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.backend.blog.component.ObserverPost;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.dto.PostRequestDTO;
import com.coursevm.backend.blog.util.SlugUtil;
import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.core.util.MapperUtil;
import com.coursevm.entity.blog.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
interface PostDAO extends BaseDAO<Post, Long> {
}

@Service
@Transactional
public class PostService extends AbstractBaseService<Post, Long> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private TagService tagService;

    static QPost qPost = QPost.post;
    static QCategory qCategory = QCategory.category;
    static QTag qTag = QTag.tag;

    @Override
    public PostDAO getRepository() {
        return postDAO;
    }

    public String createSlug(Long id, String name) {
        SlugUtil.Friendly<Post> slug = urlFriendly();
        return SlugUtil.makeSlug(id, name, slug);
    }

    @Override
    public Post save(Post entity) {
        if (CollectionUtils.isNotEmpty(entity.getTagsStr())) {
            Set<Tag> tags = tagService.saveAsString(entity.getTagsStr());
            entity.setTags(tags);
        }

        PostDTO postOld = null;
        if (entity.getId() != null) {
            //convert to DTO to remove session's hibernate
            postOld = MapperUtil.map(findOne(entity.getId()), PostDTO.class);
        }
        Post post = super.saveAndFlush(entity);
        ObserverPost observerPost = ObserverPost
                .source(this)
                .post(post)
                .postOld(postOld);

        applicationEventPublisher.publishEvent(observerPost);

        return post;
    }

    public Page<Post> findAll(PostRequestDTO param, Pageable pageable) {
        JPAQuery<Post> query = getQuery()
                .selectFrom(qPost)
                .distinct();

        if (StringUtils.isNotBlank(param.getCateQuery())) {
            query.join(qPost.categories, qCategory)
                    .where(qCategory.categorySlug.equalsIgnoreCase(param.getCateQuery()));
        }

        if (StringUtils.isNotBlank(param.getTagQuery())) {
            query.join(qPost.tags, qTag)
                    .where(qTag.categorySlug.equalsIgnoreCase(param.getTagQuery()));
        }

        if (pageable.getSort() == Sort.unsorted()) {
            query.orderBy(qPost.postId.desc());
        }
        return super.findAll(query, pageable);
    }

    @Override
    public Long delete(Long id) {

        ObserverPost observerPost = ObserverPost.source(this).post(findOne(id));

        Long idDeleted = super.delete(id);

        //count after deleted
        applicationEventPublisher.publishEvent(observerPost);

        return idDeleted;
    }

    private SlugUtil.Friendly<Post> urlFriendly() {
        return (Long id, String slug) -> {
            BooleanBuilder builder = new BooleanBuilder(qPost.postSlug.equalsIgnoreCase(slug));

            if (id != null && id > 0) {
                builder.and(qPost.postId.ne(id));
            }
            Post post = getQuery().selectFrom(qPost).where(builder).fetchFirst();

            return Optional.ofNullable(post);
        };
    }
}
