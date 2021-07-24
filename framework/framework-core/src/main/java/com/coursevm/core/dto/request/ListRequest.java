package com.coursevm.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ListRequest<T> extends BaseRequest {

    private List<T> params;

    private ListRequest() {
    }

    private ListRequest(List<T> params) {
        this.params = params;
    }

    private  <E> ListRequest<T> setParam(List<E> sources, Class<T> destinationType) {
        this.params = new ArrayList<>();
        sources.forEach(e -> this.params.add(map(e, destinationType)));

        return this;
    }

    public List<T> getParam() {
        return params;
    }

    public <E> List<E> getParam(Class<E> destinationType) {
        return params.stream().map(param -> map(param, destinationType)).collect(Collectors.toList());
    }

    public static <T, E> ListRequest<T> of(List<E> sources, Class<T> destinationType) {
        return new ListRequest<T>().setParam(sources, destinationType);
    }

    public static <T> ListRequest<T> of(List<T> params) {
        return new ListRequest<>(params);
    }
}
