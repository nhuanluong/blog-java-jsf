/*
 * Created on 2021.07.12 (y.M.d) 22:07
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.repository.CommandRepository;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.entity.blog.entity.Media;
import com.coursevm.entity.blog.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nhuan Luong
 */
interface MediaDAO extends BaseDAO<Media, Long> {

}
@Service
@Transactional
public class MediaService extends AbstractBaseService<Media, Long> {

    @Autowired
    private MediaDAO mediaDAO;

    @Override
    public CommandRepository<Media, Long> getRepository() {
        return mediaDAO;
    }
}
