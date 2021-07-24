/*
 * Created on 2020.12.11 (y.M.d) 09:00
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.repository;

import com.coursevm.core.backend.entity.BaseEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author Nhuan Luong
 */
@NoRepositoryBean
public class BaseDAOImpl<T, ID extends Serializable> extends GenericWriteRepositoryImpl<T, ID> {

    public BaseDAOImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    protected JPQLQuery<T> createQuery(Predicate... predicate) {

        /*if (TenantContextThreadLocal.get() == null) {
            return (JPQLQuery<T>) querydsl.createQuery(path).where(predicate);
        }*/
        /*if (anyClazzMatch()) {
            return (JPQLQuery<T>) querydsl.createQuery(path).where(predicate).where(builder.get("isDeleted").eq(false));
        } else {
        }*/
        return (JPQLQuery<T>) querydsl.createQuery(path).where(predicate);
    }

    @Override
    protected void maskTenantOrg(T entity) {
        //ignore
    }

    @Override
    protected void maskTenant(T entity) {
        //ignore
    }

    @Override
    protected JPQLQuery<T> queryByTenant(JPQLQuery<T> query) {
        return query;
    }

    @Override
    protected Class<?>[] getSuperClazz() {
        return new Class[]{BaseEntity.class};
    }
}
