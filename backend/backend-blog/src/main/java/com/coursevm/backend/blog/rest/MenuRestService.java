/*
 * Created on 2021.07.22 (y.M.d) 07:56
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.dto.MenuDTO;
import com.coursevm.backend.blog.service.MenuService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.response.ListResult;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.entity.blog.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nhuan Luong
 */
@Component
public class MenuRestService {

    @Autowired
    private MenuService menuService;

    public ObjectResult<MenuDTO> findById(Long id) {
        return ObjectResult.of(menuService.findById(id).orElse(null), MenuDTO.class);
    }

    public ObjectResult<MenuDTO> save(ObjectRequest<MenuDTO> data) {
        return ObjectResult.of(menuService.save(data.getParam(Menu.class)), MenuDTO.class);
    }

    public ObjectResult<Boolean> delete(Long id) {
        menuService.delete(id);
        return ObjectResult.of(true);
    }

    public ListResult<MenuDTO> findAll() {
        return ListResult.of(menuService.findAll(), MenuDTO.class);
    }
}
