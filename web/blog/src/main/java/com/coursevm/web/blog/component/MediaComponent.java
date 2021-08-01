package com.coursevm.web.blog.component;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.dto.MediaDTO;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.rest.CategoryRestService;
import com.coursevm.backend.blog.rest.MediaRestService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.request.PageableRequest;
import com.coursevm.core.dto.request.PagedRequest;
import com.coursevm.core.dto.response.PagedResult;
import com.coursevm.core.frontend.primefaces.GenericLazyDataModel;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.web.blog.util.TreeNodeUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component(value = "mediaComponent")
@ELBeanName(value = "mediaComponent")
@Scope(value = "session")
public class MediaComponent {

    @Autowired
    private MediaRestService mediaRestService;

    private GenericLazyDataModel<MediaDTO> lstMedia;
    private MediaDTO selectedMedia;

    @PostConstruct
    public void initData() {
        lstMedia = loadData();
    }

    public GenericLazyDataModel<MediaDTO> loadData() {

        return new GenericLazyDataModel<>() {
            @Override
            public List<MediaDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

                PagedRequest pageRequest = FacesContextUtil.getListPageRequest(first, pageSize, sortBy, filterBy);

                PagedResult<MediaDTO> result = mediaRestService.findAll(PageableRequest.of(null, pageRequest));

                setRowCount((int) result.getTotalElements());

                return result.getPagedList();
            }
        };
    }
}