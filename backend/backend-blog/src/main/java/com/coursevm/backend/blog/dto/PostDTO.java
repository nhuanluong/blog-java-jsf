package com.coursevm.backend.blog.dto;

import com.coursevm.core.backend.entity.BaseEntity;
import com.coursevm.core.base.entity.MarkUpdated;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.entity.blog.entity.PostMeta;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

@Getter
@Setter
@ToString(callSuper = true)
public class PostDTO extends BaseEntity implements NodeType, MarkUpdated {

    public PostDTO() {
        categories = new HashSet<>();
        tags = new ArrayList<>();
        tagsStr = new ArrayList<>();
        postCreatedDate = LocalDateTime.now();
    }

    private Long postId;

    private String postAuthor;

    private LocalDateTime postCreatedDate;

    private LocalDateTime postLastUpdated;

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
        setPostLastUpdated(time);
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

    private Map<String, String> attributes = new HashMap<>();

    public Boolean isAllowComment;

    public Boolean isAllowPingBack;

    public String featuredImage;

    public Boolean isTickToTop;

    public Boolean isPendingReview;

    public void setIsAllowComment(Boolean allowComment) {
        isAllowComment = allowComment;
        attributes.put(PostMeta.allowComment, isAllowComment + "");
    }

    public void setIsAllowPingBack(Boolean allowPingBack) {
        isAllowPingBack = allowPingBack;
        attributes.put(PostMeta.allowPingBack, isAllowPingBack + "");
    }

    public Boolean getIsAllowComment() {
        return BooleanUtils.toBoolean(attributes.getOrDefault(PostMeta.allowComment, "true"));
    }

    public Boolean getIsAllowPingBack() {
        return BooleanUtils.toBoolean(attributes.getOrDefault(PostMeta.allowPingBack, "true"));
    }

    public String getFeaturedImage() {
        return attributes.getOrDefault(PostMeta.featuredImage, "");
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
        attributes.put(PostMeta.featuredImage, featuredImage + "");
    }

    public Boolean getIsTickToTop() {
        return BooleanUtils.toBoolean(attributes.getOrDefault(PostMeta.isStickToTop, "false"));
    }

    public void setIsTickToTop(Boolean tickToTop) {
        isTickToTop = tickToTop;
        attributes.put(PostMeta.isStickToTop, isTickToTop + "");
    }

    public Boolean getIsPendingReview() {
        return BooleanUtils.toBoolean(attributes.getOrDefault(PostMeta.isPendingReview, "false"));
    }

    public void setIsPendingReview(Boolean pendingReview) {
        isPendingReview = pendingReview;
        attributes.put(PostMeta.isPendingReview, isPendingReview + "");
    }

    @SneakyThrows
    public String displayDate() {

        if (postCreatedDate == null) return "";

        return TimeAgo.using(DateUtils.parseDate(postCreatedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), "dd-MM-yyyy HH:mm").getTime());
    }
}
