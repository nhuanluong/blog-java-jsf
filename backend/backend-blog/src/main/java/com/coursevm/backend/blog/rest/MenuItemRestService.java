/*
 * Created on 2021.07.22 (y.M.d) 07:56
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.dto.MenuItemDTO;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.service.MenuItemService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.response.ListResult;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.entity.blog.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Nhuan Luong
 */
@Component
public class MenuItemRestService {

    @Autowired
    private MenuItemService menuItemService;

    public ObjectResult<MenuItemDTO> findById(Long id) {
        return ObjectResult.of(menuItemService.findById(id).orElse(null), MenuItemDTO.class);
    }

    public ObjectResult<MenuItemDTO> save(ObjectRequest<MenuItemDTO> data) {
        return ObjectResult.of(menuItemService.save(data.getParam(MenuItem.class)), MenuItemDTO.class);
    }

    public ObjectResult<Boolean> delete(Long id) {
        menuItemService.delete(id);
        return ObjectResult.of(true);
    }

    public ListResult<MenuItemDTO> findByMenuId(Long id){
        return ListResult.of(menuItemService.findByMenuId(id), MenuItemDTO.class);
    }
}
