package com.coursevm.backend.blog.dto;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.core.base.entity.MarkUpdated;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.entity.blog.entity.Category;
import com.coursevm.entity.blog.entity.Tag;
import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

@Getter
@Setter
public class PostDTO extends BaseEntity implements NodeType, MarkUpdated {

    public PostDTO() {
        categories = new HashSet<>();
        tags = new ArrayList<>();
        tagsStr = new ArrayList<>();
    }

    private Long postId;

    private String postAuthor;

    private String postContent;

    @NotBlank(message = "Title must not be null")
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

    private Set<CategoryDTO> categories;

    private List<TagDTO> tags;

    private List<String> tagsStr;

    public List<String> getTagsStr() {
        if (CollectionUtils.isNotEmpty(tags) && CollectionUtils.isEmpty(tagsStr)) {
            tagsStr = getTags().stream()
                    .map(TermDTO::getCategoryName)
                    .collect(Collectors.toList());
        }
        return tagsStr;
    }
}
