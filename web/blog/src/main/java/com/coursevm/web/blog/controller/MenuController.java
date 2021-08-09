/*
 * Created on 2021.08.07 (y.M.d) 17:04
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.web.blog.controller;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.dto.MenuDTO;
import com.coursevm.backend.blog.dto.MenuItemDTO;
import com.coursevm.backend.blog.rest.CategoryRestService;
import com.coursevm.backend.blog.rest.MenuItemRestService;
import com.coursevm.backend.blog.rest.MenuRestService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.web.blog.component.CategoryComponent;
import com.coursevm.web.blog.util.TreeNodeUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nhuan Luong
 */
@Scope(value = "session")
@Component(value = "menuController")
@ELBeanName(value = "menuController")
@Getter
@Setter
public class MenuController {

    private TreeNode rootRight;
    private TreeNode[] selectedNodes;
    private TreeNode selectedNode;
    private MenuItemDTO customLink;
    private List<MenuDTO> menu;
    private MenuDTO menuSelect;
    private MenuDTO editableMenu;

    @Autowired
    private CategoryComponent categoryComponent;

    @Autowired
    private CategoryRestService categoryRestService;

    @Autowired
    private MenuRestService menuRestService;

    @Autowired
    private MenuItemRestService menuItemRestService;

    @PostConstruct
    public void initData() {
        customLink = new MenuItemDTO();
        menu = menuRestService.findAll().getResult();
        rootRight = TreeNodeUtil.createTree(new ArrayList<>(), true, MenuItemDTO.class);
        categoryComponent.initData();
        editableMenu = new MenuDTO();
        menuSelect = null;
    }

    public void onMenuChange() {
        rootRight = TreeNodeUtil.createTree(new ArrayList<>(), true, MenuItemDTO.class);
        if (menuSelect != null) {
            List<MenuItemDTO> items = menuItemRestService.findByMenuId(menuSelect.getId()).getResult();
            rootRight = TreeNodeUtil.createTree(items, true, MenuItemDTO.class);

            TreeUtils.sortNode(rootRight, Comparator.comparing(o -> ((MenuItemDTO) (((TreeNode) o).getData())).getPostMenuOrder()));
        }
    }

    public void editMenuItem(Object data) {

    }

    public String getCategory(Long id) {

        if (id == null) return "";

        Optional<CategoryDTO> cats = categoryComponent.getCategories()
                .stream()
                .filter(item -> item.getCategoryId().equals(id))
                .findFirst();

        if (cats.isPresent()) return cats.get().getName();

        CategoryDTO cat = categoryRestService.findOne(id).getResult();

        if (cat != null) return cat.getName();

        return "";
    }

    public void deleteMenuItem(Object data) {

        if (data == null) return;

        removeMenuItemUI(rootRight, (MenuItemDTO) data);
    }

    private void removeMenuItemUI(TreeNode node, MenuItemDTO itemRemove) {

        if (node == null) return;

        Iterator<TreeNode> child = node.getChildren().iterator();

        while (child.hasNext()) {
            TreeNode item = child.next();

            if (itemRemove.equals(item.getData())) {
                if (CollectionUtils.isNotEmpty(item.getChildren())) {
                    item.getChildren().forEach(i -> {
                        i.setParent(rootRight);
                    });
                }
                child.remove();
                return;
            }
            if (CollectionUtils.isNotEmpty(item.getChildren())) {
                removeMenuItemUI(item, itemRemove);
            }
        }
    }

    public void preRenderIndex() {
        initData();
    }

    public void addMenu() {
        if (editableMenu != null) {
            MenuDTO select = menuRestService.save(ObjectRequest.of(editableMenu)).getResult();
            editableMenu = new MenuDTO();
            menu = menuRestService.findAll().getResult();
            initData();
            menuSelect = select;
        }
    }

    public void addMenuCustomLink() {

        if (customLink == null) return;

        customLink.setMenu(menuSelect);
        customLink.setMenuItemObject("custom");
        new DefaultTreeNode(customLink, rootRight);

        customLink = new MenuItemDTO();
    }

    public void addToMenuContext() {
        if (selectedNodes != null && selectedNodes.length > 0) {
            Arrays.stream(selectedNodes).forEach(item -> {
                CategoryDTO data = (CategoryDTO) item.getData();
                MenuItemDTO newMenuItem = new MenuItemDTO();
                newMenuItem.setPostTitle(data.getCategoryName());
                newMenuItem.setPostSlug(data.getSlug());
                newMenuItem.setMenuItemObject("category");
                newMenuItem.setMenuItemObjectId(data.getCategoryId());

                newMenuItem.setMenu(menuSelect);

                new DefaultTreeNode(newMenuItem, rootRight);
            });
            selectedNodes = new TreeNode[0];
            categoryComponent.initData();
        }
    }

    public void saveMenu() {
        if (rootRight != null) {
            List<TreeNode> child = rootRight.getChildren();

            if (CollectionUtils.isEmpty(child)) {
                menuSelect.setMenuItems(new ArrayList<>());
                menuRestService.save(ObjectRequest.of(menuSelect));

                FacesContextUtil.showInfo("Updated");

                return;
            }

            List<MenuItemDTO> saveList = new ArrayList<>();
            addChild(saveList, rootRight, false);

            AtomicInteger i = new AtomicInteger();
            saveList.forEach(item->{
                item.setPostMenuOrder(i.incrementAndGet());
            });

            saveList.forEach(item-> {
                if (item.getPostId() == null) {
                    MenuItemDTO tem = menuItemRestService.save(ObjectRequest.of(item)).getResult();
                    item.setPostId(tem.getPostId());
                }
            });

            saveList.clear();

            addChild(saveList, rootRight, true);

            if (CollectionUtils.isNotEmpty(saveList)) {
                menuSelect.setMenuItems(saveList);
                menuRestService.save(ObjectRequest.of(menuSelect));

                FacesContextUtil.showInfo("Updated");
            }
        }
    }

    private void addChild(List<MenuItemDTO> list, TreeNode node, boolean setParent) {

        if (node == null) return;

        MenuItemDTO root = (MenuItemDTO) node.getData();

        if (StringUtils.isNotBlank(root.getName())) {
            list.add(root);
        }

        List<TreeNode> child = node.getChildren();

        if (CollectionUtils.isEmpty(child)) return;

        child.forEach(item -> {
            MenuItemDTO data = (MenuItemDTO) item.getData();
            if (setParent) {
                data.setPostParentId(root.getPostId());
            }
            list.add(data);
            if (CollectionUtils.isNotEmpty(item.getChildren())) {
                addChild(list, item, setParent);
            }
        });
    }

    public void deleteMenu() {
        if (menuSelect != null) {
            menuRestService.delete(menuSelect.getId());
            initData();
        }
    }
}
