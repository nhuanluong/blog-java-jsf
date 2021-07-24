package com.coursevm.core.dto.request;

import com.coursevm.core.util.MapperUtil;
import org.dozer.Mapper;

public abstract class BaseRequest {

    protected static Mapper modelMapper = MapperUtil.getMapper();

    protected static <E, T> T map(E source, Class<T> destinationType) {
        T map = null;
        try {
            map = modelMapper.map(source, destinationType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
