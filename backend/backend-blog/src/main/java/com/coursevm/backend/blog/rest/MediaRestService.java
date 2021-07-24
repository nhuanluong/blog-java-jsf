/*
 * Created on 2021.07.22 (y.M.d) 07:56
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.rest;

import com.coursevm.backend.blog.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nhuan Luong
 */
@Component
public class MediaRestService {

    @Autowired
    private MediaService mediaService;

}
