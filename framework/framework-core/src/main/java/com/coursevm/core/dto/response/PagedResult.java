package com.coursevm.core.dto.response;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PagedResult<T> extends BaseResult {

    private List<T> pagedList;
    private long totalElements;

    private PagedResult() {
    }

    private PagedResult<T> setResult(Page<T> result) {
        this.pagedList = result.getContent();
        this.totalElements = result.getTotalElements();

        return this;
    }

    private <E> PagedResult<T> setResult(Page<E> source, Class<?> destinationType) {
        this.pagedList = Lists.newArrayList();

        pagedList = source.getContent().stream()
                .map(item -> (T) map(item, destinationType))
                .collect(Collectors.toList());

        this.totalElements = source.getTotalElements();

        return this;
    }

    public static <T> PagedResult<T> of(Page<T> result) {
        return new PagedResult<T>().setResult(result);
    }

    public static <T, E> PagedResult<T> of(Page<E> source, Class<?> destinationType) {
        return new PagedResult<T>().setResult(source, destinationType);
    }
}
