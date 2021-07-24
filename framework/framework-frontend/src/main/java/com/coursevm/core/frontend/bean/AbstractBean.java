package com.coursevm.core.frontend.bean;

import com.coursevm.core.backend.repository.CommandRepository;
import com.coursevm.core.backend.service.CommandService;
import com.coursevm.core.base.entity.NodeType;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.frontend.util.FacesContextUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
public abstract class AbstractBean<T extends NodeType> {

    public abstract void initData();
    
    public abstract CommandRepository<T, Long> service();

    public abstract boolean validateData();

    protected T add(T node) {

        boolean isValid = validateData();

        if (!isValid) return node;

        Long nodeId = node.getId();

        node = service().save(node);

        FacesContextUtil.addMessageInfo(nodeId);

        initData();

        return node;
    }

    protected T edit(T node) {

        boolean isValid = validateData();

        if (!isValid) return node;

        Long nodeId = node.getId();

        service().save(node);

        FacesContextUtil.addMessageInfo(nodeId);

        return node;
    }

    protected void delete(Long id) {
        if (id != null) {
            service().deleteById(id);
        }
        initData();
    }

    protected void updateSlug(T node) {

        if (node == null || StringUtils.isBlank(node.getName())) return;

        if (StringUtils.isNotBlank(node.getSlug())) {
            node.setSlug(TextUtil.makeUrlFriendly(node.getSlug()));

            return;
        }
        //String slug = service().makeSlug(node.getId(), node.getName());
        //node.setSlug(slug);
    }

    protected List<T> loadTerms() {
        return service().findAll();
    }

}