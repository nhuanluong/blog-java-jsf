/*
 * Created on 2021.07.31 (y.M.d) 14:33
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.backend.blog.dto;

import com.coursevm.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
public class PostRequestDTO extends BaseDTO {
    private String cateQuery;
    private String tagQuery;
}
