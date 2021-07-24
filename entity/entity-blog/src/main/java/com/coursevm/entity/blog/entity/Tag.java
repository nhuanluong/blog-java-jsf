/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
@Entity
@Getter
@Setter
@DiscriminatorValue("tag")
public class Tag extends Term {

    /*@ManyToMany(mappedBy = "tags")
    private Set<Post> posts;*/
}
