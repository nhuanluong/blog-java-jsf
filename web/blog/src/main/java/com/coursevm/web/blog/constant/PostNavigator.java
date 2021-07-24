package com.coursevm.web.blog.constant;

public interface PostNavigator extends HomeNavigator{
    String index = HomeNavigator.home + "/views/post/index.xhtml";
    String edit = HomeNavigator.home + "/views/post/edit.xhtml";
}