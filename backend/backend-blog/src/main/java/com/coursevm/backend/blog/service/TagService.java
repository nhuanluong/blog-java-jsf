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
import com.coursevm.core.base.entity.MarkUpdated;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.entity.blog.entity.Category;
import com.coursevm.entity.blog.entity.QTag;
import com.coursevm.entity.blog.entity.Tag;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Nhuan Luong
 */
interface TagDAO extends BaseDAO<Tag, Long> {

    Tag findFirst1CategoryByCategorySlugAndCategoryIdNot(@Param("slug") String slug, @Param("id") Long id);

    Tag findFirst1CategoryByCategorySlug(@Param("slug") String slug);
}

@Service
public class TagService extends AbstractBaseService<Tag, Long> {

    @Autowired
    private TagDAO tagDAO;

    @Override
    public TagDAO getRepository() {
        return tagDAO;
    }

    private static QTag qTag = QTag.tag;

    public boolean isExistsSlug(String slugFriendly, Long tagId) {
        return checkSlugExistsNotBelongTo(slugFriendly, tagId).isPresent();
    }

    public Tag save(Tag entity) {

        Tag byName = findByName(entity.getName(), entity.getId());

        if (byName != null) return byName;

        NodeType nodeType = entity;
        if (StringUtils.isBlank(nodeType.getSlug())) {
            nodeType.setSlug(makeSlug(nodeType.getId(), nodeType.getName()));
        }

        if (entity instanceof MarkUpdated) {
            MarkUpdated n = (MarkUpdated) entity;
            n.setLastUpdated(LocalDateTime.now());
        }
        return tagDAO.save(entity);
    }

    public String makeSlug(Long categoryId, String termName) {
        termName = TextUtil.makeUrlFriendly(termName);

        return makeSlug(categoryId, termName, 0, termName.length());
    }

    private String makeSlug(Long id, String slugFriendly, int count, int length) {

        Optional<Tag> tag = checkSlugExistsNotBelongTo(slugFriendly, id);

        if (tag.isEmpty()) return slugFriendly;

        String slug = slugFriendly.substring(0, length) + "-" + (++count);

        slug = makeSlug(id, slug, count, length);

        return slug;
    }

    public Optional<Tag> findBySlug(String slug) {

        if (StringUtils.isBlank(slug)) return Optional.empty();

        BooleanBuilder builder = new BooleanBuilder().and(qTag.categorySlug.equalsIgnoreCase(slug));

        return Optional.ofNullable(getQuery().selectFrom(qTag).where(builder).fetchFirst());
    }

    public Optional<Tag> checkSlugExistsNotBelongTo(String slug, Long id) {

        if (id == null) return findBySlug(slug);

        BooleanBuilder builder = new BooleanBuilder()
                .and(qTag.categorySlug.equalsIgnoreCase(slug))
                .and(qTag.categoryId.ne(id));

        return Optional.ofNullable(getQuery().selectFrom(qTag).where(builder).fetchFirst());
    }

    public List<Tag> complete(String name, int limit) {

        name = StringUtils.trimToEmpty(name);

        if (limit < 0 || limit > 1000) limit = 10;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qTag.categoryName.containsIgnoreCase(name));

        return getQuery().selectFrom(qTag).where(builder).limit(limit).fetch();
    }

    public Set<Tag> saveAsString(List<String> tagsStr) {
        Set<String> tags = new HashSet<>(tagsStr);
        Set<Tag> result = new HashSet<>();

        tags.forEach(t -> {
            Optional<Tag> bySlug = findBySlug(t);
            if (bySlug.isPresent()) {
                result.add(bySlug.get());
            } else {
                Tag tn = new Tag();
                tn.setCategoryName(t);
                tn = save(tn);
                result.add(tn);
            }
        });
        return result;
    }

    public Tag findByName(String tag, Long id) {
        BooleanBuilder builder = new BooleanBuilder(qTag.categoryName.equalsIgnoreCase(StringUtils.trimToEmpty(tag)));
        if (id != null) {
            builder.and(qTag.categoryId.ne(id));
        }
        return getQuery().selectFrom(qTag).where(builder).fetchFirst();
    }

    public void updateCount(Long categoryId, long count) {
        Tag tag = findOne(categoryId);
        if (tag != null) {
            tag.setCategoryCount(count);
            tagDAO.save(tag);
        }
    }
}
