/*
 * Created on 2020.07.10 (y.M.d) 09:36
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.base.entity;

import java.io.Serializable;

/**
 * @author Nhuan Luong
 */
public interface NodeType extends BaseId, Serializable {
    String getType();
    void setType(String type);
    String getName();
    String getSlug();
    void setSlug(String slug);
}