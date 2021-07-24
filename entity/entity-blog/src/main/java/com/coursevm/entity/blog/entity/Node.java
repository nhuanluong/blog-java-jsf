package com.coursevm.entity.blog.entity;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.core.base.entity.MarkUpdated;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.trim;

@Getter
@Setter
@Table(name = BlogSchema.posts, catalog = BlogSchema.name)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "postType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
@Entity
public abstract class Node extends BaseEntity implements NodeType, MarkUpdated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column
    private String postAuthor;

    @Column
    private LocalDateTime postCreatedDate;

    @Column
    private LocalDateTime postLastUpdated;

    @Column
    private String postContent;

    @Column
    @NotBlank(message = "Title must not be null")
    private String postTitle;

    @Column
    private String postExcerpt;

    @Column
    private String postStatus;

    @Column
    private String postPassword;

    @Column
    private String postSlug;

    @Column(insertable = false, updatable = false)
    private String postType;

    @Column
    private String postMimeType;

    @Column
    private Long postParentId;

    @Column
    private String postGUID;

    @Column
    private Integer postMenuOrder;

    @Column
    private String postCommentStatus;

    @Column
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
        setPostLastUpdated(time);
    }
}
