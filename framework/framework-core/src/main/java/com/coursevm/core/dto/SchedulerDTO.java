/*
 * Created on 2020.09.30 (y.M.d) 17:09
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nhuan Luong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedulerDTO {
    private Long tenantId;
    private Long tenantOrgId;
    private String cron;
}
