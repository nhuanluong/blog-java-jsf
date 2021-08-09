/*
 * Created on 2021.07.12 (y.M.d) 21:16
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.dto;

import com.coursevm.entity.blog.entity.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
@NoArgsConstructor
public class MenuDTO extends TermDTO {
    private List<MenuItemDTO> menuItems = new ArrayList<>();
}