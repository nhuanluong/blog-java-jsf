///*
// * Created on 2021.07.12 (y.M.d) 22:07
// *
// * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
// * This software is the proprietary information of VietInfo Company.
// *
// */
//package com.coursevm.core.backend.service;
//
//import com.coursevm.core.base.entity.MarkUpdated;
//import com.coursevm.core.base.entity.NodeType;
//import org.apache.commons.collections4.IterableUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.PagingAndSortingRepository;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author Nhuan Luong
// */
//public abstract class AbstractService<T, ID> implements CommandService<T, ID> {
//
//    public abstract PagingAndSortingRepository<T, ID> getRepository();
//
//    public abstract String getType();
//
//    public Page<T> findAll(Pageable pageable) {
//        return getRepository().findAll(pageable);
//    }
//
//    @Override
//    public List<T> findAll() {
//
//        Iterable<T> all = getRepository().findAll();
//
//        if (!all.iterator().hasNext()) return Collections.emptyList();
//
//        return IterableUtils.toList(all);
//    }
//
//    @Override
//    public T save(T entity) {
//        if (entity instanceof NodeType) {
//            NodeType nodeType = (NodeType) entity;
//            nodeType.setType(getType());
//            String slug = nodeType.getSlug();
//            if (StringUtils.isBlank(slug)) {
//                nodeType.setSlug(makeSlug(nodeType.getId(), nodeType.getName()));
//            }
//        }
//        if (entity instanceof MarkUpdated) {
//            MarkUpdated n = (MarkUpdated) entity;
//            n.setLastUpdated(LocalDateTime.now());
//        }
//        return getRepository().save(entity);
//    }
//
//    @Override
//    public void delete(ID id) {
//        getRepository().deleteById(id);
//    }
//
//    @Override
//    public Optional<T> findOne(ID id) {
//        return getRepository().findById(id);
//    }
//}
