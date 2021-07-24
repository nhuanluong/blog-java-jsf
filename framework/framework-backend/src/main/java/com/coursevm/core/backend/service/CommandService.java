/*
 * Created on 2021.07.16 (y.M.d) 21:32
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.service;

/**
 * @author Nhuan Luong
 */
public interface CommandService<T, ID> extends QueryService<T, ID> {

    T save(T entity);

    void delete(ID id);

    String makeSlug(Long termId, String termName);

}