/*
 * Created on 2021.08.07 (y.M.d) 16:26
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.service;

import com.coursevm.backend.blog.util.SlugUtil;
import com.coursevm.backend.blog.util.UrlFriendly;
import com.coursevm.core.backend.dao.BaseDAO;
import com.coursevm.core.backend.repository.CommandRepository;
import com.coursevm.core.backend.service.AbstractBaseService;
import com.coursevm.entity.blog.entity.Menu;
import com.coursevm.entity.blog.entity.QMenu;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

interface MenuDAO extends BaseDAO<Menu, Long> {
}

@Service
@Transactional
public class MenuService extends AbstractBaseService<Menu, Long> {

    @Autowired
    private MenuDAO menuDAO;

    @Override
    public CommandRepository<Menu, Long> getRepository() {
        return menuDAO;
    }

    static QMenu qMenu = QMenu.menu;
    @Override
    public Menu save(Menu entity) {
        if (StringUtils.isBlank(entity.getSlug())) {
            entity.setSlug(SlugUtil.makeSlug(entity.getId(), entity.getName(), new MenuFriendLy()));
        }
        return super.save(entity);
    }

    class MenuFriendLy implements UrlFriendly<Menu> {
        @Override
        public Menu makeSlug(Long id, String name) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qMenu.categorySlug.containsIgnoreCase(name));
            if (id != null && id > 0) {
                builder.and(qMenu.categoryId.ne(id));
            }
            return getQuery().selectFrom(qMenu).where(builder).fetchFirst();
        }
    }
}