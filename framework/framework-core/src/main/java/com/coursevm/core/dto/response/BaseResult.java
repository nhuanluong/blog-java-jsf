package com.coursevm.core.dto.response;

import com.coursevm.core.util.MapperUtil;
import lombok.Getter;
import lombok.Setter;
import org.dozer.Mapper;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@Getter
@Setter
public abstract class BaseResult {

    protected static Mapper modelMapper = MapperUtil.getMapper();

    private int code;
    private String msg;

    public String getMsg() {
        return trimToEmpty(msg);
    }

    protected static <E, T> T map(E source, Class<T> destinationType) {
        T map = null;
        try {
            map = modelMapper.map(source, destinationType);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return map;
    }
}
