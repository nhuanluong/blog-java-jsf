/*
 * Created on 2021.07.18 (y.M.d) 19:20
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.component;

import java.util.Set;

/**
 * @author Nhuan Luong
 */
public interface TermTree<T> {
    T getParent();
    Set<T> getChildren();
}