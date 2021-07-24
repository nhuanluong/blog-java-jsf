/*
 * Created on 2020.09.09 (y.M.d) 17:13
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @author Nhuan Luong
 */
@NoRepositoryBean
public interface CommandRepository<T, ID> extends ReadRepository<T, ID> {

    void flush();

    <S extends T> S save(S entity);

    <S extends T> S saveAndFlush(S entity);

    <S extends T> List<S> saveAll(Iterable<S> entities);

    void deleteAll();

    void delete(T entity);

    void deleteById(ID id);

    void deleteAllInBatch();

    void deleteInBatch(Iterable<T> entities);

    void deleteAll(Iterable<? extends T> entities);
}
