package com.coursevm.web.blog.component;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.rest.CategoryRestService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.web.blog.util.TreeNodeUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;
import org.primefaces.PF;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Component(value = "categoryComponent")
@Scope(value = "session")
public class CategoryComponent {

    @Autowired
    private CategoryRestService categoryRestService;

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
        return categoryRestService.findAll().getResult();
    }


    public boolean validateData() {
        if (category == null || StringUtils.isBlank(category.getName())) {
            FacesContextUtil.showError("Please type title!");
            return false;
        }
        return true;
    }

    public void initCat() {
        category = new CategoryDTO();
    }

    public void addCatFromPost() {

        boolean isValid = validateData();

        if (!isValid) return ;

        CategoryDTO cate = categoryRestService.save(ObjectRequest.of(category)).getResult();

        initData();

        category = cate;
    }
}