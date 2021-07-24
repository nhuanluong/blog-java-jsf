package com.coursevm.core.frontend.primefaces;

import com.coursevm.core.base.entity.BaseId;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.LazyDataModel;

import java.util.List;

/**
 * @author Nhuan Luong
 */
@Getter
public abstract class GenericLazyDataModel<T> extends LazyDataModel<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public String getRowKey(T entity) {
        Long id = ((BaseId) entity).getId();
        return id.toString();
    }

    @Override
    public T getRowData(String rowKey) {

        List<T> rows = getWrappedData();

        if (CollectionUtils.isEmpty(rows)) return null;

        for (T row : rows) {
            if (row instanceof BaseId) {
                BaseId r = ((BaseId) row);
                if (r.getId().toString().equals(rowKey))
                    return row;
            }
        }
        return null;
    }
}
