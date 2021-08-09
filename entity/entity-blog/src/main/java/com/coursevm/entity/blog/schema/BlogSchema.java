/*
 * Created on 2021.07.12 (y.M.d) 21:45
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.schema;

/**
 * @author Nhuan Luong
 */
public interface BlogSchema {
    String name = "blog";
    String category = "Categories";
    String posts = "Posts";
    String categoryPostMap = "CategoryPostMap";
    String tagPostMap = "TagPostMap";
    String menuPostMap = "MenuPostMap";
    String options = "Options";
    String postMeta = "PostMeta";
    String userMeta = "UserMeta";
    String users = "Users";
}
