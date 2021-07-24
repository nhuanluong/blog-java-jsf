/*
 * Created on 2020.09.09 (y.M.d) 17:31
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.bean;

import com.coursevm.core.backend.repository.BaseDAOImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author Nhuan Luong
 */
public class CustomRepositoryFactory extends JpaRepositoryFactory {

    private final EntityManager entityManager;

    public CustomRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {

        RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

        Object queryableFragment = getTargetRepositoryViaReflection(BaseDAOImpl.class, entityInformation, entityManager);

        fragments = fragments.append(RepositoryFragment.implemented(queryableFragment));

        return fragments;
    }
}
