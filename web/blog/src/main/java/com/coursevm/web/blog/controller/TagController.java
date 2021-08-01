package com.coursevm.web.blog.controller;

import com.coursevm.backend.blog.dto.TagDTO;
import com.coursevm.backend.blog.rest.TagRestService;
import com.coursevm.backend.blog.service.TagService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.entity.blog.entity.Category;
import com.coursevm.entity.blog.entity.Tag;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Scope(value = "session")
@Component(value = "tagController")
@ELBeanName(value = "tagController")
@Getter
@Setter
public class TagController {

    @Autowired
    private TagRestService tagService;

    private TagDTO tag;
    private List<TagDTO> tags;

    private TreeNode root;
    private TreeNode selectedNode;

    @PostConstruct
    public void initData() {
        tag = new TagDTO();
        tags = loadTags();
        root = createTree(tags, true);
    }

    public List<TagDTO> loadTags() {
        return tagService.findAll().getResult();
    }

    public void addTag() {

        boolean isValid = validateData();

        if (!isValid) return ;

        Long nodeId = tag.getId();

        tag = tagService.save(ObjectRequest.of(tag)).getResult();

        FacesContextUtil.addMessageInfo(nodeId);

        initData();
    }

    public void deleteTag() {
        tagService.delete(FacesContextUtil.getRequestParamAsLong("categoryId"));
        initData();
    }

    public void editTag() {

        tags = loadTags();

        Long tagId = FacesContextUtil.getRequestParamAsLong("categoryId");

        if (CollectionUtils.isNotEmpty(tags) && tagId != null) {
            Optional<TagDTO> cate = tags.stream()
                    .filter(item -> item.getId().equals(tagId))
                    .findFirst();

            if (cate.isPresent()) {
                tag = cate.get();
                tags.remove(tag);
            }
        }
    }

    public void updateSlug() {

        if (tag == null || StringUtils.isBlank(tag.getName())) return;

        if (StringUtils.isNotBlank(tag.getSlug())) {
            tag.setSlug(TextUtil.makeUrlFriendly(tag.getSlug()));

            return;
        }
        String slug = tagService.makeSlug(tag.getId(), tag.getName());

        tag.setSlug(slug);
    }

    public boolean validateData() {

        if (tag == null) {
            FacesContextUtil.addMessageError("Chưa nhập thông tin");
            return false;
        }

        if (StringUtils.isBlank(tag.getCategorySlug())) {
            updateSlug();
        }

        if (StringUtils.isBlank(tag.getName())) {
            FacesContextUtil.showError("Tag name must not be null!");
            return false;
        }

        TagDTO tagName = tagService.findByName(tag.getName(), tag.getId()).getResult();

        if (tagName != null) {
            FacesContextUtil.showError("Duplicated tag name!");
            return false;
        }

        boolean isExists = tagService.isExistsSlug(tag.getCategorySlug(), tag.getCategoryId());

        if (isExists) {
            FacesContextUtil.addMessageError("Slug đã tồn tại");
            return false;
        }

        if (StringUtils.isBlank(tag.getCategorySlug())){
            FacesContextUtil.addMessageError("Slug đang bị rỗng");
            return false;
        }
        return true;
    }

    //region private
    protected TreeNode createTree(List<TagDTO> tags, boolean expanded) {

        if (CollectionUtils.isEmpty(tags)) return new DefaultTreeNode(new Category(), null);

        TreeNode root = new DefaultTreeNode(new Category(), null);
        root.setExpanded(expanded);

        tags.forEach(p -> {
            new DefaultTreeNode(p, root);
        });
        return root;
    }
    //endregion

    public void preRenderView() {
        if (FacesContextUtil.isNotPostback()) {
            initData();
        }
    }
}