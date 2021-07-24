package com.coursevm.web.blog.component;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.dto.TagDTO;
import com.coursevm.backend.blog.rest.TagRestService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.frontend.util.FacesContextUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component(value = "tagComponent")
@Scope(value = "session")
public class TagComponent {

    @Autowired
    private TagRestService tagRestService;

    private TagDTO tag;
    private List<TagDTO> tags;
    private List<String> tagsName;

    @PostConstruct
    public void initData() {
        tags = loadTags();
    }

    public List<TagDTO> loadTags() {
        return tagRestService.findAll().getResult();
    }

    public void addCategory() {

        boolean isValid = validateData();

        if (!isValid) return;

        Long nodeId = tag.getId();

        tag = tagRestService.save(ObjectRequest.of(tag)).getResult();

        FacesContextUtil.addMessageInfo(nodeId);

        initData();
    }

    public void updateSlug() {

        if (tag == null || StringUtils.isBlank(tag.getName())) return;

        if (StringUtils.isNotBlank(tag.getSlug())) {
            tag.setSlug(TextUtil.makeUrlFriendly(tag.getSlug()));
            return;
        }
        String slug = tagRestService.makeSlug(tag.getId(), tag.getName());
        tag.setSlug(slug);
    }

    public boolean validateData() {
        if (tag == null || StringUtils.isBlank(tag.getName())) {
            FacesContextUtil.showError("Please type title!");
            return false;
        }
        return true;
    }

    public List<TagDTO> complete(String name, int limit) {
        return tagRestService.complete(name, limit).getResult();
    }

    public List<String> getTagsName() {
        if (CollectionUtils.isEmpty(tagsName)) tagsName = new ArrayList<>();
        return tagsName;
    }
    //endregion

    public void initTag() {
        tag = new TagDTO();
    }

    public void addTagFromPost() {

        boolean isValid = validateData();

        if (!isValid) return ;

        TagDTO t = tagRestService.save(ObjectRequest.of(tag)).getResult();

        initData();

        tag = t;
    }
}