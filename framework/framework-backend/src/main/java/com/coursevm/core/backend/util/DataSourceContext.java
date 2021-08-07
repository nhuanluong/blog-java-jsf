/*
 * Created on 2021.08.07 (y.M.d) 09:55
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author Nhuan Luong
 */
@Component
public class DataSourceContext {
    @Autowired
    DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }
}
