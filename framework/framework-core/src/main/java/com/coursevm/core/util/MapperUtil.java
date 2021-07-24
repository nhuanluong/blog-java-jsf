/*
 * Created on 2020.08.21 (y.M.d) 09:51
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.util;

import org.dozer.DozerBeanMapper;

/**
 * @author Nhuan Luong
 */
public class MapperUtil {

    private static final DozerBeanMapper instance = new DozerBeanMapper();

    public static DozerBeanMapper getMapper() {
        return instance;
    }
}