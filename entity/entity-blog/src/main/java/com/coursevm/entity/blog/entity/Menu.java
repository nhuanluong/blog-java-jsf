/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.entity.blog.entity;

import com.coursevm.core.component.TermTree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

/**
 * @author Nhuan Luong
 */
@Entity
@Getter
@Setter
@DiscriminatorValue("nav_menu")
@NoArgsConstructor
public class Menu extends Term {

    // inverse side: it has a mappedBy attribute, and can't decide how the association is mapped, since the other side already decided it.
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<MenuItem> menuItems = new ArrayList<>();

}
