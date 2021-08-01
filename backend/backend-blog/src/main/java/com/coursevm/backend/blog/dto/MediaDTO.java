package com.coursevm.backend.blog.dto;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.core.base.entity.MarkUpdated;
import com.coursevm.core.base.entity.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.trim;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MediaDTO extends BaseEntity implements NodeType, MarkUpdated {

    public MediaDTO() {
    }

    private Long postId;

    private String postAuthor;

    private String postContent;

    private String postTitle;

    private String postExcerpt;

    private String postStatus;

    private String postPassword;

    private String postSlug;

    private String postType;

    private String postMimeType;

    private Long postParentId;

    private String postGUID;

    private Integer postMenuOrder;

    private String postCommentStatus;

    private Long postCommentCount;

    public void setPostContent(String postContent) {
        this.postContent = trim(postContent);
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = trim(postTitle);
    }

    public void setPostExcerpt(String postExcerpt) {
        this.postExcerpt = trim(postExcerpt);
    }

    public void setPostSlug(String postSlug) {
        this.postSlug = trim(postSlug);
    }

    @Override
    public Long getId() {
        return postId;
    }

    @Override
    public String getName() {
        return getPostTitle();
    }

    @Override
    public String getSlug() {
        return getPostSlug();
    }

    @Override
    public void setSlug(String slug) {
        setPostSlug(slug);
    }

    @Override
    public void setType(String type) {
        setPostType(type);
    }

    @Override
    public String getType() {
        return getPostType();
    }

    @Override
    public void setLastUpdated(LocalDateTime time) {

    }
}
