/*
 * Created on 2021.08.08 (y.M.d) 23:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.util;

/**
 * @author Nhuan Luong
 */
@FunctionalInterface
public interface UrlFriendly<T> {
    T makeSlug(Long id, String name);
}

