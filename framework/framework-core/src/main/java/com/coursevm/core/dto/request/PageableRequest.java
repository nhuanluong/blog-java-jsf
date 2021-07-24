package com.coursevm.core.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@ToString
public class PageableRequest<T> extends BaseRequest {

    private T param;
    private PagedRequest pagedRequest;

    private PageableRequest() {
    }

    private PageableRequest(PagedRequest pageable) {
        pagedRequest = pageable;
    }

    private PageableRequest(T param, PagedRequest pageable) {
        this.param = param;
        pagedRequest = pageable;
    }

    public <E> PageableRequest<T> setParam(E source, Class<T> destinationType) {
        this.param = map(source, destinationType);
        return this;
    }

    public <E> E getParam(Class<E> destinationType) {
        return map(this.param, destinationType);
    }

    private PageRequest getPaged(PagedRequest pageable) {
        return PageRequest.of(pageable.getNum(), pageable.getSize(), pageable.getSort() == null ? Sort.unsorted() : pageable.getSort());
    }

    public static <T> PageableRequest<T> of(PagedRequest pageable) {
        return new PageableRequest<>(pageable);
    }

    public static <T> PageableRequest<T> of(T param, PagedRequest pageable) {
        return new PageableRequest<>(param, pageable);
    }

    public static <T, E> PageableRequest<T> of(E source, Class<T> destinationType) {
        return new PageableRequest<T>().setParam(source, destinationType);
    }

    public Pageable getPageable() {
        return getPaged(pagedRequest);
    }
}
