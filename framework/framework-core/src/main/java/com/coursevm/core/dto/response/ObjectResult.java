package com.coursevm.core.dto.response;

public class ObjectResult<T> extends BaseResult {

    private T result;

    private ObjectResult() {
    }

    public T getResult() {
        return result;
    }

    public boolean isPresent() {
        return result != null;
    }

    private ObjectResult<T> setResult(T result) {
        this.result = result;
        return this;
    }

    private <E> ObjectResult<T> setResult(E source, Class<?> destinationType) {
        this.result = source == null ? null : (T) map(source, destinationType);
        return this;
    }

    public static <T, E> ObjectResult<T> of(E source, Class<?> destinationType) {
        return new ObjectResult<T>().setResult(source, destinationType);
    }

    public static <T> ObjectResult<T> of(T result) {
        return new ObjectResult<T>().setResult(result);
    }
}
