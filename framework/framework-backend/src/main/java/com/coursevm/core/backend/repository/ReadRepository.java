/*
 * Created on 2020.09.09 (y.M.d) 17:12
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.repository;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nhuan Luong
 */
@NoRepositoryBean
public interface ReadRepository<T, ID> extends Repository<T, ID> {

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(JPQLQuery<T> query, Pageable pageable);

    Page<T> findAll(Predicate predicate, Pageable pageable);

    Page<T> findAllDistinct(JPQLQuery<T> query, Pageable pageable);

    Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable);

    List<T> findAll();

    List<T> findAll(Sort sort);

    List<T> findAll(JPQLQuery<T> query);

    List<T> findAll(Predicate predicate);

    List<T> findAllById(Iterable<ID> ids);

    List<T> findAllDistinct(JPQLQuery<T> query);

    List<T> findAll(OrderSpecifier<?>... orders);

    List<T> findAll(Predicate predicate, Sort sort);

    List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    long count();

    long count(Predicate predicate);

    /*Long count(JPQLQuery<T> query);*/

    T getOne(ID id);

    T getOne(Predicate predicate);

    Optional<T> findById(ID id);

    Optional<T> findOne(Predicate predicate);

    boolean existsById(ID id);

    boolean exists(Predicate predicate);
}
