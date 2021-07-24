/*
 * Created on 2020.11.26 (y.M.d) 08:42
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.dto.request;

import com.coursevm.core.jsonb.JsonbOrder;
import com.coursevm.core.jsonb.JsonbSort;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

/**
 * @author Nhuan Luong
 */
@Getter
@Setter
@ToString
public class PagedRequest extends BaseRequest {
    private int num;
    private int size;
    private JsonbSort jsonbSort;

    private PagedRequest() {
    }

    private PagedRequest(int num, int size) {
        this.num = num;
        this.size = size;
    }

    public static PagedRequest of(int num, int size) {
        return new PagedRequest(num, size);
    }

    public static PagedRequest of(int num, int size, Sort sort) {
        return new PagedRequest(num, size, sort);
    }

    private PagedRequest(int num, int size, Sort sort) {
        this.num = num;
        this.size = size;

        if (sort != null) {

            if (jsonbSort == null) jsonbSort = new JsonbSort();

            sort.stream().forEach(item -> {
                Sort.Direction d = item.getDirection();
                String p = item.getProperty();
                jsonbSort.getOrders().add(new JsonbOrder(d.name(), p));
            });
        }
    }

    public Sort getSort() {

        if (jsonbSort == null) return null;

        Sort sort = null;

        for (JsonbOrder item : jsonbSort.getOrders()) {
            if (sort == null) {
                sort = Sort.by(Sort.Direction.fromString(item.getDirection()), item.getProperty());
                continue;
            }
            sort.and(Sort.by(Sort.Direction.fromString(item.getDirection()), item.getProperty()));
        }
        return sort;
    }
}
