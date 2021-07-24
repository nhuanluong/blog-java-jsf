///*
// * Created on 2021.07.22 (y.M.d) 21:31
// *
// * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
// * This software is the proprietary information of VietInfo Company.
// *
// */
//package com.coursevm.backend.blog.dao;
//
//import com.coursevm.core.backend.dao.BaseDAO;
//import com.coursevm.entity.blog.entity.Post;
//import com.coursevm.entity.blog.entity.Tag;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author Nhuan Luong
// */
//public interface PostDAO extends BaseDAO<Post, Long> {
//
//    Optional<Post> findFirstByPostSlugAndPostIdNot(@Param("slug") String slug, @Param("id") Long id);
//
//    Optional<Post> findFirstByPostSlug(@Param("slug") String slug);
//}
