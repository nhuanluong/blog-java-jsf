/*
 * Created on 2021.07.26 (y.M.d) 20:55
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import lombok.experimental.UtilityClass;

/**
 * @author Nhuan Luong
 */
@UtilityClass
public class PostMeta {
    public final String featuredImage = "featured_image";
    public final String allowComment = "allow_comment";
    public final String allowPingBack = "allow_ping_back";
    public final String isStickToTop = "stick_to_top";
    public final String isPendingReview = "pending_review";
}
