/*
 * Created on 2020.12.11 (y.M.d) 10:26
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.repository;

import com.coursevm.core.base.context.TenantContextThreadLocal;
import com.coursevm.core.base.entity.AuditBaseEntity;
import com.coursevm.core.base.entity.BaseDelete;
import com.coursevm.core.base.entity.MarkableAndAuditBaseEntity;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.util.ProxyUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Nhuan Luong
 */
public abstract class GenericWriteRepositoryImpl<T, ID extends Serializable> extends GenericReadRepositoryImpl<T, ID> implements CommandRepository<T, ID> {

    public GenericWriteRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    protected abstract void maskTenant(T entity);

    protected abstract void maskTenantOrg(T entity);

    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Transactional
    public void delete(T entity) {

        Assert.notNull(entity, "Entity must not be null!");

        if (entityInformation.isNew(entity)) {
            return;
        }

        if (entity instanceof BaseDelete) {
            BaseDelete baseDelete = (BaseDelete) entity;
            baseDelete.setIsDeleted(true);

            em.merge(entity);

            return;
        }

        Class<?> type = ProxyUtils.getUserClass(entity);

        T existing = (T) em.find(type, entityInformation.getId(entity));

        // if the entity to be deleted doesn't exist, delete is a NOOP
        if (existing == null) {
            return;
        }
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Transactional
    public void deleteAll(Iterable<? extends T> entities) {

        if (IterableUtils.isEmpty(entities)) return;

        for (T entity : entities) {
            delete(entity);
        }
    }

    @Transactional
    public void deleteAll() {
        for (T element : findAll()) {
            delete(element);
        }
    }

    @Transactional
    public <S extends T> S save(S entity) {
        //maskTenant(entity);
        //maskTenantOrg(entity);
        maskEntityNew(entity);
        //updateAuditFields(entity);

        return simpleJpaRepository.save(entity);
    }

    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {

        Assert.notNull(entities, "Entities must not be null!");

        List<S> result = new ArrayList<>();

        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Transactional
    public void flush() {
        em.flush();
    }

    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        S result = save(entity);
        flush();

        return result;
    }

    public void deleteInBatch(Iterable<T> entities) {
        Assert.notNull(entities, "Entities must not be null!");

        while (entities.iterator().hasNext()) {
            delete(entities.iterator().next());
        }
    }

    public void deleteAllInBatch() {
        simpleJpaRepository.deleteAllInBatch();
    }

    protected <S extends T> void updateAuditFields(S entity) {

        Long userId = getUserId();

        if (entityInformation.isNew(entity)) {
            if (isSubClazz(AuditBaseEntity.class)) {
                ((AuditBaseEntity) entity).setCreatedBy(userId);
                ((AuditBaseEntity) entity).setCreatedDate(new Date());
            } else if (isSubClazz(MarkableAndAuditBaseEntity.class)) {
                ((MarkableAndAuditBaseEntity) entity)
                        .withCreatedBy(userId)
                        .withCreatedDate(new Date());
            }
        } else {
            if (isSubClazz(AuditBaseEntity.class)) {
                ((AuditBaseEntity) entity).setModifiedBy(userId);
                ((AuditBaseEntity) entity).setModifiedDate(new Date());
            } else if (isSubClazz(MarkableAndAuditBaseEntity.class)) {
                ((MarkableAndAuditBaseEntity) entity)
                        .withModifiedBy(userId)
                        .withModifiedDate(new Date());
            }
        }
    }

    protected void maskEntityNew(T entity) {
        if (entityInformation.isNew(entity) && entity instanceof BaseDelete) {
            BaseDelete markableField = (BaseDelete) entity;
            markableField.setIsDeleted(false);
        }
    }

    private Long getUserId() {
        return TenantContextThreadLocal.get() == null ? null : TenantContextThreadLocal.get().getUserMasterId();
    }
}
