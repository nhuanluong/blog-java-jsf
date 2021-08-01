/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.backend.blog.dto.PostRequestDTO;
import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.entity.blog.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public boolean isExistsSlug(String slugFriendly, Long termId) {
        return checkSlugNotById(slugFriendly, termId).isPresent();
    }

    public Optional<Post> checkSlugNotById(String slug, Long id) {
        BooleanBuilder builder = new BooleanBuilder(qPost.postSlug.equalsIgnoreCase(slug));
        if (id != null && id > 0) {
            builder.and(qPost.postId.ne(id));
        }
        Post post = getQuery().selectFrom(qPost).where(builder).fetchFirst();

        return Optional.ofNullable(post);
    }

    public Optional<Post> findBySlug(String slug) {
        return Optional.ofNullable(getQuery()
                .selectFrom(qPost)
                .where(qPost.postSlug.equalsIgnoreCase(slug))
                .fetchFirst()
        );
    }

    public String makeSlug(Long id, String name) {

        if (StringUtils.isBlank(name)) return name;

        name = TextUtil.makeUrlFriendly(name);

        return makeSlug(id, name, 0, name.length());
    }

    private String makeSlug(Long categoryId, String slugFriendly, int count, int length) {

        Optional<Post> post = checkSlugNotById(slugFriendly, categoryId);

        if (post.isEmpty()) return slugFriendly;

        String slug = slugFriendly.substring(0, length) + "-" + (++count);

        slug = makeSlug(categoryId, slug, count, length);//recursion

        return slug;
    }

    @Override
    public Post save(Post entity) {
        if (CollectionUtils.isNotEmpty(entity.getTagsStr())) {
            Set<Tag> tags = tagService.saveAsString(entity.getTagsStr());
            entity.setTags(tags);
        }
        return super.save(entity);
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
        return super.findAll(query, pageable);
    }
}
