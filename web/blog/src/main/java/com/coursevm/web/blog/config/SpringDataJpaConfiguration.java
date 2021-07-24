/*
 * Created on 2020.09.09 (y.M.d) 09:32
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.web.blog.config;

import com.coursevm.core.backend.bean.CustomRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Nhuan Luong
 */
@Configuration
@EnableJpaRepositories(value =
        {
                "com.coursevm.backend.*.service"
        },
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@EnableTransactionManagement
public class SpringDataJpaConfiguration {
}
