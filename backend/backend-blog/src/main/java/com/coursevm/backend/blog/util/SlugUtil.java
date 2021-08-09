/*
 * Created on 2021.08.08 (y.M.d) 23:14
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.util;

import com.coursevm.core.common.util.TextUtil;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @author Nhuan Luong
 */
@UtilityClass
public class SlugUtil {

    public <T> String makeSlug(Long id, String name, Friendly<T> checkSlugMethod) {
        name = TextUtil.makeUrlFriendly(name);

        return makeSlug(id, name, 0, name.length(), checkSlugMethod);
    }

    private <T> String makeSlug(Long categoryId, String slugFriendly, int count, int length, Friendly<T> checkSlugMethod) {

        Optional<T> node = checkSlugMethod.getSlug(categoryId, slugFriendly);

        if (node.isEmpty()) return slugFriendly;

        String slug = slugFriendly.substring(0, length) + "-" + (++count);

        slug = makeSlug(categoryId, slug, count, length, checkSlugMethod);//recursion

        return slug;
    }

    @FunctionalInterface
    public interface Friendly<T> {
        Optional<T> getSlug(Long id, String name);
    }
}
