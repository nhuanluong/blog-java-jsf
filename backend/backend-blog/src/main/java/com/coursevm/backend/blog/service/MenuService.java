/*
 * Created on 2021.08.07 (y.M.d) 16:26
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.repository.CommandRepository;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.entity.blog.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nhuan Luong
 */
interface MenuDAO extends BaseDAO<MenuItem, Long> {

}
@Service
@Transactional
public class MenuService extends AbstractBaseService<MenuItem, Long> {

    @Autowired
    private MenuDAO menuDAO;

    @Override
    public CommandRepository<MenuItem, Long> getRepository() {
        return menuDAO;
    }
}
