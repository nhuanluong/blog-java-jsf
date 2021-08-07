/*
 * Created on 2020.12.11 (y.M.d) 10:26
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.repository;

import com.google.common.collect.Lists;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.*;

import static org.springframework.data.jpa.repository.query.QueryUtils.COUNT_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;

/**
 * @author Nhuan Luong
 */
public abstract class GenericReadRepositoryImpl<T, ID extends Serializable> implements ReadRepository<T, ID> {

    private static final EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;

    protected final JpaEntityInformation<T, ID> entityInformation;
    protected final EntityPath<T> path;
    protected final Querydsl querydsl;
    protected final PathBuilder<T> builder;
    protected final EntityManager em;
    protected final SimpleJpaRepository<T, ID> simpleJpaRepository;
    protected final QuerydslJpaPredicateExecutor<T> querydslJpaPredicateExecutor;

    public GenericReadRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
                                     EntityManager entityManager) {
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
        this.querydslJpaPredicateExecutor = new QuerydslJpaPredicateExecutor<>(entityInformation, entityManager, resolver, null);
        this.simpleJpaRepository = new SimpleJpaRepository<>(entityInformation, entityManager);
    }

    protected abstract JPQLQuery<T> createQuery(Predicate... predicate);

    protected abstract JPQLQuery<T> queryByTenant(JPQLQuery<T> query);

    protected abstract Class<?>[] getSuperClazz();

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (null == pageable) {
            return new PageImpl<>(findAll());
        }
        JPQLQuery<T> jpqlQuery = createQuery();
        JPQLQuery<T> query = querydsl.applyPagination(pageable, jpqlQuery);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, jpqlQuery::fetchCount);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {

        Assert.notNull(predicate, "Predicate must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");

        final JPQLQuery<?> countQuery = createQuery(predicate);
        JPQLQuery<T> query = querydsl.applyPagination(pageable, createQuery(predicate).select(path));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
    }

    @Override
    public Page<T> findAll(JPQLQuery<T> jpqlQuery, Pageable pageable) {
        queryByTenant(jpqlQuery);
        JPQLQuery<T> query = querydsl.applyPagination(pageable, jpqlQuery);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, jpqlQuery::fetchCount);
    }

    @Override
    public Page<T> findAllDistinct(JPQLQuery<T> query, Pageable pageable) {
        JPQLQuery<T> queryTenant = queryByTenant(query);
        JPQLQuery<T> jpqlQuery = queryTenant.distinct();
        JPQLQuery<T> paginationQuery = querydsl.applyPagination(pageable, jpqlQuery);

        return PageableExecutionUtils.getPage(paginationQuery.fetch(), pageable, jpqlQuery::fetchCount);
    }

    @Override
    public Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable) {
        JPQLQuery<T> countQuery = createQuery(predicate);
        JPQLQuery<T> query = querydsl.applyPagination(pageable, createQuery(predicate));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
    }

    @Override
    public List<T> findAll() {
        return createQuery().fetch();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return querydsl.applySorting(sort, createQuery()).fetch();
    }

    @Override
    public List<T> findAll(JPQLQuery<T> query) {
        queryByTenant(query);
        return query.fetch();
    }

    @Override
    public List<T> findAll(Predicate predicate) {
        Assert.notNull(predicate, "Predicate must not be null!");
        return createQuery(predicate).select(path).fetch();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {

        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }

        return createQuery(builder.get("id").in(ids)).fetch();
    }

    @Override
    public List<T> findAllDistinct(JPQLQuery<T> query) {
        queryByTenant(query);
        return query.distinct().fetch();
    }

    @Override
    public List<T> findAll(OrderSpecifier<?>... orders) {

        Assert.notNull(orders, "Order specifiers must not be null!");

        return executeSorted(createQuery(new Predicate[0]).select(path), orders);
    }

    @Override
    public List<T> findAll(Predicate predicate, Sort sort) {

        Assert.notNull(predicate, "Predicate must not be null!");
        Assert.notNull(sort, "Sort must not be null!");

        return executeSorted(createQuery(predicate).select(path), sort);
    }

    @Override
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {

        Assert.notNull(predicate, "Predicate must not be null!");
        Assert.notNull(orders, "Order specifiers must not be null!");

        return executeSorted(createQuery(predicate).select(path), orders);
    }

    @Override
    public long count() {
        String countQuery = String.format(COUNT_QUERY_STRING, "x", "%s");
        if (allClazzMatch()) {
            countQuery = countQuery + " where isDeleted = 0";
        }
        String queryString = getQueryString(countQuery, entityInformation.getEntityName());

        return em.createQuery(queryString, Long.class).getSingleResult();
    }

    @Override
    public long count(Predicate predicate) {
        return createQuery(predicate).fetchCount();
    }

    public Long count(JPQLQuery<T> query) {
        return createQuery().fetchCount();
    }

    @Override
    public T getOne(ID id) {
        return simpleJpaRepository.getById(id);
    }

    @Override
    public T getOne(Predicate predicate) {
        return createQuery(predicate).fetchFirst();
    }

    @Override
    public Optional<T> findById(ID id) {

        if (id == null) return Optional.empty();

        return simpleJpaRepository.findById(id);
    }

    @Override
    public Optional<T> findOne(Predicate predicate) {

        Assert.notNull(predicate, "Predicate must not be null!");

        try {
            return Optional.ofNullable(createQuery(predicate).select(path).fetchOne());
        } catch (NonUniqueResultException ex) {
            throw new IncorrectResultSizeDataAccessException(ex.getMessage(), 1, ex);
        }
    }

    @Override
    public boolean existsById(ID id) {
        return simpleJpaRepository.existsById(id);
    }

    @Override
    public boolean exists(Predicate predicate) {
        return createQuery(predicate).fetchCount() > 0;
    }

    protected <E> boolean isSubClazz(Class<E> superClass) {

        Class<?> clazz = path.getType();

        if (clazz == superClass) return true;

        while (clazz != Object.class) {

            if (clazz == superClass) return true;

            clazz = clazz.getSuperclass();
        }
        return false;
    }

    protected <E> boolean isSubClazz(Class<?> clazz, Class<E> superClass) {

        if (clazz == superClass) return true;

        while (clazz != Object.class) {

            if (clazz == superClass) return true;

            clazz = clazz.getSuperclass();
        }
        return false;
    }

    protected Boolean allClazzMatch() {
        return Arrays.stream(getSuperClazz())
                .allMatch(clazz -> isSubClazz(path.getType(), clazz));
    }

    protected <U> Boolean isSubClass(Class<U> clazz) {
        Class<?> subClass = path.getType();
        try {
            subClass.asSubclass(clazz);
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    private List<T> executeSorted(JPQLQuery<T> query, OrderSpecifier<?>... orders) {
        return executeSorted(query, new QSort(orders));
    }

    private List<T> executeSorted(JPQLQuery<T> query, Sort sort) {
        return querydsl.applySorting(sort, query).fetch();
    }
}
