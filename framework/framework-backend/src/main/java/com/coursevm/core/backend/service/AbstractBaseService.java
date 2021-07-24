/*
 * Created on 2020.12.11 (y.M.d) 10:26
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.service;

import com.coursevm.core.backend.repository.CommandRepository;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * @author Nhuan Luong
 */
public abstract class AbstractBaseService<T, ID extends Serializable> {

    @PersistenceContext
    private EntityManager entityManager;

    public abstract CommandRepository<T, ID> getRepository();

    public T findOne(ID id) {
        return getRepository().getOne(id);
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void saveAll(List<T> entity) {
        getRepository().saveAll(entity);
    }

    public ID delete(ID id) {
        getRepository().deleteById(id);
        return id;
    }

    public void delete(T entity) {
        getRepository().delete(entity);
    }

    public List<T> findAllById(List<ID> ids) {
        return getRepository().findAllById(ids);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public List<T> findAll(JPQLQuery<T> query) {
        return getRepository().findAll(query);
    }

    public Page<T> findAll(JPQLQuery<T> query, Pageable pageable) {
        return getRepository().findAll(query, pageable);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public JPAQueryFactory getQuery() {
        return new JPAQueryFactory(getEntityManager());
    }
}