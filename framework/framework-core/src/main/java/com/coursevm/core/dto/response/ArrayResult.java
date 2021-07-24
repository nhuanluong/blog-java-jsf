package com.coursevm.core.dto.response;

import com.coursevm.core.dto.BaseDTO;
import com.google.common.collect.Lists;

import java.util.List;

public class ArrayResult<T> extends BaseResult {

    private T[] result;

    public T[] getResult() {
        return result;
    }

    public ArrayResult<T> setResult(T[] result) {
        this.result = result;
        return this;
    }

    public <E> ArrayResult<T> setResult(E[] source, Class<? extends BaseDTO> destinationType) {
        List<T> list = Lists.newArrayList();
        for (E item : source) {
            list.add((T) map(item, destinationType));
        }
        this.result = (T[]) list.toArray();

        return this;
    }

    public static <T> ArrayResult<T> of(T[] result) {
        return new ArrayResult<T>().setResult(result);
    }

    public static <T, E> ArrayResult<T> of(E[] source, Class<? extends BaseDTO> destinationType) {
        return new ArrayResult<T>().setResult(source, destinationType);
    }
}
