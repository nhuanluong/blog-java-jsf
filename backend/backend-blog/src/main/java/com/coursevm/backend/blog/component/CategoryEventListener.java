/*
 * Created on 2021.08.07 (y.M.d) 08:58
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.component;

import com.coursevm.backend.blog.service.CategoryPostMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Nhuan Luong
 */
@Component
public class CategoryEventListener implements ApplicationListener<ObserverPost> {

    @Autowired
    private CategoryPostMapService categoryPostMapService;

    @Override
    public void onApplicationEvent(ObserverPost event) {
        categoryPostMapService.updateCount(event);
    }
}
