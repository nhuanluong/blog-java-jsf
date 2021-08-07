/*
 * Created on 2021.08.07 (y.M.d) 08:46
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.component;

import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.core.util.MapperUtil;
import com.coursevm.entity.blog.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.util.Optional;

/**
 * @author Nhuan Luong
 */
@Getter
public class ObserverPost extends ApplicationEvent {

    private Optional<PostDTO> post = Optional.empty();
    private Optional<PostDTO> postOld = Optional.empty();

    public ObserverPost(Object source) {
        super(source);
    }

    public static ObserverPost source(Object source) {
        return new ObserverPost(source);
    }

    public ObserverPost post(PostDTO post) {
        this.post = Optional.ofNullable(post);
        return this;
    }

    public ObserverPost post(Post post) {
        this.post = Optional.ofNullable(MapperUtil.map(post, PostDTO.class));
        return this;
    }

    public ObserverPost postOld(PostDTO post) {
        this.postOld = Optional.ofNullable(post);
        return this;
    }

    public ObserverPost postOld(Post post) {
        this.postOld = Optional.ofNullable(MapperUtil.map(post, PostDTO.class));
        return this;
    }
}
