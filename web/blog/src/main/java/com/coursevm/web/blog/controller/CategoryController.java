package com.coursevm.web.blog.controller;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.rest.CategoryRestService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.web.blog.util.TreeNodeUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Scope(value = "session")
@Component(value = "categoryController")
@ELBeanName(value = "categoryController")
@Getter
@Setter
public class CategoryController {

    @Autowired
    private CategoryRestService categoryService;

    private CategoryDTO category;
    private List<CategoryDTO> categories;

    private TreeNode root;
    private TreeNode selectedNode;

    @PostConstruct
    public void initData() {
        category = new CategoryDTO();
        categories = loadCategories();
        root = TreeNodeUtil.createTree(categories, true, CategoryDTO.class);
    }

    public List<CategoryDTO> loadCategories() {
        return categoryService.findAll().getResult();
    }

    public void addCategory() {

        boolean isValid = validateData();

        if (!isValid) return;

        Long nodeId = category.getId();

        category = categoryService.save(ObjectRequest.of(category)).getResult();

        FacesContextUtil.addMessageInfo(nodeId);

        initData();
    }

    public void deleteCategory() {
        categoryService.delete(FacesContextUtil.getRequestParamAsLong("categoryId"));
        initData();
    }

    public void editCategory() {

        categories = loadCategories();

        Long categoryId = FacesContextUtil.getRequestParamAsLong("categoryId");

        if (CollectionUtils.isNotEmpty(categories) && categoryId != null) {
            Optional<CategoryDTO> cate = categories.stream()
                    .filter(item -> item.getId().equals(categoryId))
                    .findFirst();

            if (cate.isPresent()) {
                category = cate.get();
                categories.remove(category);
            }
        }

        if (CollectionUtils.isNotEmpty(categories)) {
            Set<CategoryDTO> children = category.getSubCategories();
            if (CollectionUtils.isNotEmpty(children)) {
                TreeNodeUtil.removeChild(categories, children);
            }
        }
    }

    public void updateSlug() {

        if (category == null || StringUtils.isBlank(category.getName())) return;

        if (StringUtils.isNotBlank(category.getSlug())) {
            category.setSlug(TextUtil.makeUrlFriendly(category.getSlug()));

            return;
        }
        String slug = categoryService.makeSlug(category.getId(), category.getName());
        category.setSlug(slug);
    }

    public boolean validateData() {

        if (category == null) {
            FacesContextUtil.showError("Please fill the data");
            return false;
        }

        if (StringUtils.isBlank(category.getCategoryName())) {
            FacesContextUtil.showError("Title mus not be null");
            return false;
        }

        if (StringUtils.isBlank(category.getCategorySlug())) {
            updateSlug();
        }

        if (StringUtils.isBlank(category.getCategorySlug())) {
            FacesContextUtil.addMessageError("Slug mus not be null");
            return false;
        }

        boolean isExists = categoryService.isExistsSlug(category.getCategorySlug(), category.getCategoryId());

        if (isExists) {
            FacesContextUtil.addMessageError("Slug đã tồn tại");
            return false;
        }

        return true;
    }

    public void initCat() {
        category = new CategoryDTO();
    }

    public void addCat() {

        boolean isValid = validateData();

        if (!isValid) return;

        Long nodeId = category.getId();

        category = categoryService.save(ObjectRequest.of(category)).getResult();

        FacesContextUtil.addMessageInfo(nodeId);

        initData();
    }

    public void preRenderView() {
        if (FacesContextUtil.isNotPostback()) {
            initData();
        }
    }
}