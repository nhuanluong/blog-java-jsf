/*
 * Created on 2020.09.09 (y.M.d) 17:30
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/**
 * @author Nhuan Luong
 */
@Slf4j
public class CustomRepositoryFactoryBean<T extends JpaRepository<S, I>, S, I> extends JpaRepositoryFactoryBean<T, S, I> {

    public CustomRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    static int count = 0;

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        count++;
        System.out.println("\n.....createRepositoryFactory: " + count + " : " + entityManager);
        return new CustomRepositoryFactory(entityManager);
    }
}
