/*
 * Created on 2020.08.21 (y.M.d) 09:51
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.util;

import org.dozer.DozerBeanMapper;

import java.util.Arrays;

/**
 * @author Nhuan Luong
 */
public class MapperUtil {

    private final static DozerBeanMapper instance;

    static {
        //Arrays.asList("dozerJdk8Converters.xml")
        instance = new DozerBeanMapper(Arrays.asList("dozerJdk8Converters.xml"));
    }

    public static DozerBeanMapper getMapper() {
        return instance;
    }
}