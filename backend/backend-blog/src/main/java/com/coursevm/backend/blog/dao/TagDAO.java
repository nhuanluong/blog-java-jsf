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
//import com.coursevm.entity.blog.entity.Category;
//import com.coursevm.entity.blog.entity.Tag;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
///**
// * @author Nhuan Luong
// */
//public interface TagDAO extends BaseDAO<Tag, Long> {
//
//    Tag findFirst1CategoryByCategorySlugAndCategoryIdNot(@Param("slug") String slug, @Param("id") Long id);
//
//    Tag findFirst1CategoryByCategorySlug(@Param("slug") String slug);
//
//    List<Tag> findByCategoryNameContaining(@Param("query") String query);
//}
