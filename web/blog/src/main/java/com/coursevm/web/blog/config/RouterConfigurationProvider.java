/*
 * Created on 2021.07.18 (y.M.d) 10:52
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.web.blog.config;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

/**
 * @author Nhuan Luong
 */
@RewriteConfiguration
public class RouterConfigurationProvider extends HttpConfigurationProvider {

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Configuration getConfiguration(final ServletContext context) {

        return ConfigurationBuilder.begin()
                .addRule(Join.path("/").to("/views/post/index.xhtml"))
                .addRule(Join.path("/post").to("/views/post/index.xhtml"))
                .addRule(Join.path("/post?cat={p}").to("/views/post/index.xhtml"))
                .addRule(Join.path("/post?tag={p}").to("/views/post/index.xhtml"))
                .addRule(Join.path("/post/add").to("/views/post/edit.xhtml?status=new"))
                .addRule(Join.path("/post/edit/{p}").to("/views/post/edit.xhtml?postId={p}"))
                .addRule(Join.path("/category").to("/views/category/index.xhtml"))
                .addRule(Join.path("/tag").to("/views/tag/index.xhtml"))
                .addRule(Join.path("/media").to("/views/media/index.xhtml"))
                .addRule(Join.path("/media/edit/{p}").to("/views/media/edit.xhtml?mediaId={p}"))
                ;
    }
}
