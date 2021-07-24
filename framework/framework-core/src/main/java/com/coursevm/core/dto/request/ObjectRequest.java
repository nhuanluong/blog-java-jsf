package com.coursevm.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ObjectRequest<T> extends BaseRequest {

    private T param;

    private ObjectRequest() {
    }

    private ObjectRequest(T param) {
        this.param = param;
    }

    private <E> ObjectRequest<T> setParam(E source, Class<T> destinationType) {
        this.param = map(source, destinationType);
        return this;
    }

    public <E> E getParam(Class<E> destinationType) {
        return map(this.param, destinationType);
    }

    public static <T> ObjectRequest<T> of(T source) {
        return new ObjectRequest<>(source);
    }

    public static <T, E> ObjectRequest<T> of(E source, Class<T> destinationType) {
        return new ObjectRequest<T>().setParam(source, destinationType);
    }
}
