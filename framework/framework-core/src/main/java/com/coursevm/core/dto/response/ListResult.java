package com.coursevm.core.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ListResult<T> extends BaseResult {

    private List<T> result;

    private long total;

    private ListResult() {
    }

    public List<T> getResult() {
        return result == null ? new ArrayList<>() : result;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(result);
    }

    @JsonIgnore
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    private ListResult<T> setResult(List<T> result) {
        this.result = result;
        return this;
    }

    private <E> ListResult<T> setResult(List<E> source, Class<?> destinationType) {
        this.result = Lists.newArrayList();
        source.forEach(item -> this.result.add((T) map(item, destinationType)));

        return this;
    }

    public static <T, E> ListResult<T> of(List<E> source, Class<?> destinationType) {
        return new ListResult<T>().setResult(source, destinationType);
    }

    public static <T> ListResult<T> of(List<T> result) {
        return new ListResult<T>().setResult(result);
    }

    public static <T> ListResult<T> of(List<T> result, long total) {
        ListResult<T> lstResult = new ListResult<>();
        lstResult.setTotal(total);

        return lstResult.setResult(result);
    }

    private void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }
}
