/*
 * Created on 2021.07.19 (y.M.d) 08:45
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.base.entity;

import java.time.LocalDateTime;

/**
 * @author Nhuan Luong
 */
public interface MarkUpdated {
    void setLastUpdated(LocalDateTime time);
}
