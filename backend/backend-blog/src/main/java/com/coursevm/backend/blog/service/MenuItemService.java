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
import com.coursevm.entity.blog.entity.QMenu;
import com.coursevm.entity.blog.entity.QMenuItem;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Nhuan Luong
 */
interface MenuItemDAO extends BaseDAO<MenuItem, Long> {
}

@Service
@Transactional
public class MenuItemService extends AbstractBaseService<MenuItem, Long> {

    @Autowired
    private MenuItemDAO menuItemDAO;

    static QMenu qMenu = QMenu.menu;
    static QMenuItem qMenuItem = QMenuItem.menuItem;

    @Override
    public CommandRepository<MenuItem, Long> getRepository() {
        return menuItemDAO;
    }

    public List<MenuItem> findByMenuId(Long id) {
        JPAQuery<MenuItem> query = getQuery().selectFrom(qMenuItem)
                .join(qMenuItem.menu, qMenu)
                .where(qMenu.categoryId.eq(id))
                .orderBy(qMenuItem.postMenuOrder.asc());

        return query.fetch();
    }
}
