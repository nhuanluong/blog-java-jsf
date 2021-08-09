/*
 * Created on 2021.08.07 (y.M.d) 21:19
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.dto;

import com.coursevm.core.component.TermTree;
import com.coursevm.entity.blog.entity.Menu;
import com.coursevm.entity.blog.entity.MenuItem;
import com.coursevm.entity.blog.entity.MenuMetaField;
import com.coursevm.entity.blog.entity.PostMeta;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
public class MenuItemDTO extends PostDTO implements TermTree<MenuItemDTO> {

    private MenuItemDTO parentMenu;

    private Set<MenuItemDTO> subMenu = new HashSet<>();

    @Override
    public MenuItemDTO getParent() {
        return parentMenu;
    }

    @Override
    public Set<MenuItemDTO> getChildren() {
        return subMenu;
    }

    private MenuDTO menu;

    private String menuItemObject;

    private Long menuItemObjectId;

    public String getMenuItemObject() {
        return getAttributes().getOrDefault(MenuMetaField.menuItemObject, "");
    }

    public void setMenuItemObject(String menuItemObject) {
        this.menuItemObject = menuItemObject;
        getAttributes().put(MenuMetaField.menuItemObject, menuItemObject);
    }

    public Long getMenuItemObjectId() {
        String idStr = getAttributes().getOrDefault(MenuMetaField.menuItemObjectId, null);
        return StringUtils.isNumeric(idStr) ? Long.parseLong(idStr) : null;
    }

    public void setMenuItemObjectId(Long menuItemObjectId) {
        this.menuItemObjectId = menuItemObjectId;
        getAttributes().put(MenuMetaField.menuItemObjectId, menuItemObjectId + "");
    }
}
