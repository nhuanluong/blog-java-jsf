package com.coursevm.web.blog.constant;

public interface IndexNavigator {
    String index = "/";
    String dashboard = index + "/dashboard.xhtml";
    String post = index + "/views/post/index.xhtml";
    String category = index + "/views/category/index.xhtml";
    String tag = index + "/views/tag/index.xhtml";
}