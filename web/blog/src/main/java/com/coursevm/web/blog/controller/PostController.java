package com.coursevm.web.blog.controller;

import com.coursevm.backend.blog.dto.CategoryDTO;
import com.coursevm.backend.blog.dto.PostDTO;
import com.coursevm.backend.blog.dto.TagDTO;
import com.coursevm.backend.blog.dto.TermDTO;
import com.coursevm.backend.blog.rest.CategoryRestService;
import com.coursevm.backend.blog.rest.PostRestService;
import com.coursevm.backend.blog.rest.TagRestService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.request.PageableRequest;
import com.coursevm.core.dto.request.PagedRequest;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.core.dto.response.PagedResult;
import com.coursevm.core.frontend.primefaces.GenericLazyDataModel;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.web.blog.component.CategoryComponent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Scope(value = "session")
@Component(value = "postController")
@ELBeanName(value = "postController")
@Getter
@Setter
public class PostController {

    @Autowired
    private PostRestService postService;

    @Autowired
    private CategoryRestService categoryRestService;

    @Autowired
    private CategoryComponent categoryComponent;

    @Autowired
    private TagRestService tagRestService;

    private PostDTO post;
    private List<PostDTO> selectedPosts;
    private GenericLazyDataModel<PostDTO> posts;
    private TreeNode[] selectedNodes;

    @PostConstruct
    public void initData() {
        post = new PostDTO();
        posts = loadData();
    }

    public boolean validateData() {
        if (post == null) {
            FacesContextUtil.addMessageError("Post needs be init data");
            return false;
        }
        return true;
    }

    public void preRenderView() {
        if (FacesContextUtil.isNotPostback()) {
            //add new
            Optional<String> status = FacesContextUtil.getRequestParam("status");
            if (status.isPresent() && status.get().equalsIgnoreCase("new")) {
                post = new PostDTO();
                editPost(post);

                return;
            }
            //edit
            Long postId = FacesContextUtil.getRequestParamAsLong("postId");

            if (postId == null) FacesContextUtil.redirect("/");

            ObjectResult<PostDTO> p = postService.findById(postId);
            if (p.isPresent()) {
                post = p.getResult();
                editPost(post);
            }
        }
    }

    public void add() {

        Long id = null;

        if (!validateData()) return;

        if (post.getId() != null) {
            id = post.getId();
        }

        if (selectedNodes != null && selectedNodes.length > 0) {
            List<CategoryDTO> categories = Arrays.stream(selectedNodes)
                    .collect(Collectors.toList())
                    .stream()
                    .map(item -> (CategoryDTO) item.getData())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(categories)) {
                post.setCategories(new HashSet<>(categories));
            }
        }

        if ((selectedNodes == null || selectedNodes.length <= 0)) {
            post.setCategories(null);
        }

        if (StringUtils.isBlank(post.getSlug())) updateSlug(post);

        post = postService.save(ObjectRequest.of(post)).getResult();

        FacesContextUtil.addMessageInfo(id);

        if (id == null) {
            FacesContextUtil.redirect("/post/edit/" + post.getId());
        }
    }

    public void makeSlug() {
        updateSlug(post);
    }

    public void editPost(PostDTO post) {
        Set<CategoryDTO> cat = post.getCategories();
        categoryComponent.initData();
        if (CollectionUtils.isNotEmpty(cat)) {
            TreeNode root = categoryComponent.getRoot();
            checkRoot(cat, root);
        }
    }

    private void updateSlug(PostDTO node) {

        if (node == null || StringUtils.isBlank(node.getName())) return;

        if (StringUtils.isNotBlank(node.getSlug())) {
            node.setSlug(TextUtil.makeUrlFriendly(node.getSlug()));

            return;
        }
        String slug = postService.makeSlug(node.getId(), node.getName());

        node.setSlug(slug);
    }

    public void checkRoot(Set<CategoryDTO> sub, TreeNode node) {
        List<TreeNode> child = node.getChildren();

        if (CollectionUtils.isEmpty(child)) return;

        child.forEach(c -> {
            sub.forEach(s -> {
                CategoryDTO cat = (CategoryDTO) c.getData();
                Long categoryId = s.getCategoryId();
                Long categoryId1 = cat.getCategoryId();
                if (categoryId.equals(categoryId1)) {
                    c.setSelected(true);
                }
            });
            if (CollectionUtils.isNotEmpty(c.getChildren())) {
                checkRoot(sub, c);
            }
        });
    }

    public void deletePost(Long id) {
        postService.delete(id);
        initData();
    }

    public GenericLazyDataModel<PostDTO> loadData() {

        return new GenericLazyDataModel<>() {
            @Override
            public List<PostDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

                PagedRequest pageRequest = FacesContextUtil.getListPageRequest(first, pageSize, sortBy, filterBy);

                PagedResult<PostDTO> result = postService.findAll(PageableRequest.of(null, pageRequest));

                setRowCount((int) result.getTotalElements());

                return result.getPagedList();
            }
        };
    }

    public void addCat() {

        CategoryDTO category = categoryComponent.getCategory();

        if (category == null) {
            FacesContextUtil.showError("Category is null!");
            return;
        }

        if (CollectionUtils.isEmpty(post.getCategories())) {
            post.setCategories(new HashSet<>());
        }

        post.getCategories().add(category);

        Set<CategoryDTO> cat = post.getCategories();

        if (CollectionUtils.isEmpty(cat)) return;

        TreeNode root = categoryComponent.getRoot();
        checkRoot(cat, root);

        List<TreeNode> nodeList = new ArrayList<>();
        updateSelectedNodes(nodeList, root);

        TreeNode[] nodesSelected = new TreeNode[nodeList.size()];
        nodeList.toArray(nodesSelected);

        selectedNodes = nodesSelected;
    }

    private void updateSelectedNodes(List<TreeNode> nodeList, TreeNode root) {

        List<TreeNode> r = root.getChildren().stream()
                .filter(TreeNode::isSelected)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(r)) {
            nodeList.addAll(r);
        }

        root.getChildren().forEach(c -> {
            if (CollectionUtils.isNotEmpty(c.getChildren())) {
                updateSelectedNodes(nodeList, c);
            }
        });
    }

    public List<String> completeTag(String query) {

        List<TagDTO> tagsSearchResult = tagRestService.complete(query, 10).getResult();

        if (CollectionUtils.isEmpty(tagsSearchResult)) return new ArrayList<>();

        List<String> tagsName = tagsSearchResult.stream().map(TermDTO::getName).collect(Collectors.toList());
        List<TagDTO> tagsPost = post.getTags();
        if (CollectionUtils.isNotEmpty(tagsPost)) {
            List<String> tagsPostName = tagsPost.stream().map(TermDTO::getName).collect(Collectors.toList());
            tagsName.removeAll(tagsPostName);
        }
        return new ArrayList<>(new HashSet<>(tagsName));
    }

    public void handleTagUnSelect(UnselectEvent<String> event) {
        String tag = event.getObject();
        List<TagDTO> tags = post.getTags();

        if (StringUtils.isNotBlank(tag) && CollectionUtils.isNotEmpty(tags)) {
            for (int i = 0; i < tags.size(); i++) {
                if (tags.get(i).getCategoryName().equalsIgnoreCase(tag)) {
                    tags.remove(tags.get(i));
                    break;
                }
            }
        }
    }

    public void handleTagSelect(SelectEvent<String> event) {
        String tagSelect = event.getObject();
        if (StringUtils.isNotBlank(tagSelect) && CollectionUtils.isNotEmpty(post.getTags())) {
            for (int i = 0; i < post.getTags().size(); i++) {

                if (post.getTags().get(i).getCategoryName().equalsIgnoreCase(tagSelect)) continue;

                TagDTO tag = tagRestService.findByName(tagSelect).getResult();
                if (tag != null) {
                    getPost().getTags().add(tag);
                }
            }
        }
    }
}