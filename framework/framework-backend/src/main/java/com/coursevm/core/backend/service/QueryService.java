/*
 * Created on 2021.07.16 (y.M.d) 21:32
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.service;


import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nhuan Luong
 */
public interface QueryService<T, ID> extends Repository {

    List<T> findAll();

    Optional<T> findOne(ID id);

    boolean isExistsSlug(String slugFriendly, Long termId);
}