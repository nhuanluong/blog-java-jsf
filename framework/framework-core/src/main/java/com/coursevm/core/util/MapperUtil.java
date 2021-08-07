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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nhuan Luong
 */
public class MapperUtil {

    private final static DozerBeanMapper instance;

    static {
        instance = new DozerBeanMapper(Collections.singletonList("dozerJdk8Converters.xml"));
    }

    public static DozerBeanMapper getMapper() {
        return instance;
    }

    public static <E, T> T map(E source, Class<T> destinationType) {
        T map = null;
        try {
            map = getMapper().map(source, destinationType);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return map;
    }

    public <T, E> List<T> map(List<E> sources, Class<T> destinationType) {
        List<T> result = sources.stream()
                .map(e -> map(e, destinationType))
                .collect(Collectors.toList());
        return result;
    }
}