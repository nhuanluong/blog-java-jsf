/*
 * Created on 2021.07.12 (y.M.d) 22:03
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.repository;

import com.coursevm.entity.blog.entity.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Nhuan Luong
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
