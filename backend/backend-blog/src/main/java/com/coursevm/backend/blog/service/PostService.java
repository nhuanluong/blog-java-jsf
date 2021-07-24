/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.entity.blog.entity.Post;
import com.coursevm.entity.blog.entity.QPost;
import com.coursevm.entity.blog.entity.Tag;
import com.querydsl.core.BooleanBuilder;
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

    public Page<Post> findAll(Pageable pageable) {
        return postDAO.findAll(pageable);
    }

    @Override
    public PostDAO getRepository() {
        return postDAO;
    }

    public boolean isExistsSlug(String slugFriendly, Long termId) {
        return checkSlugNotById(slugFriendly, termId).isPresent();
    }

    public Optional<Post> checkSlugNotById(String slug, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPost.postSlug.equalsIgnoreCase(slug));
        if (id != null && id > 0) {
            builder.and(qPost.postId.ne(id));
        }
        Post value = getQuery().selectFrom(qPost).where(builder).fetchFirst();

        return Optional.ofNullable(value);
    }

    public Optional<Post> findBySlug(String slug) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(qPost.postSlug.equalsIgnoreCase(slug));

        return Optional.ofNullable(getQuery().selectFrom(qPost).where(builder).fetchFirst());
    }

    public Optional<Post> findById(Long id) {
        return postDAO.findById(id);
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

        slug = makeSlug(categoryId, slug, count, length);

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
}
