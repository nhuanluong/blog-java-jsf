package com.coursevm.web.blog.controller;

import com.coursevm.backend.blog.dto.*;
import com.coursevm.backend.blog.rest.CategoryRestService;
import com.coursevm.backend.blog.rest.PostRestService;
import com.coursevm.backend.blog.rest.TagRestService;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.request.PageableRequest;
import com.coursevm.core.dto.request.PagedRequest;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.core.dto.response.PagedResult;
import com.coursevm.core.frontend.primefaces.GenericLazyDataModel;
import com.coursevm.core.frontend.util.FacesContextUtil;
import com.coursevm.entity.blog.entity.PostMeta;
import com.coursevm.web.blog.component.CategoryComponent;
import com.coursevm.web.blog.component.MediaComponent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.TreeNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jcr.observation.Event;
import java.util.*;
import java.util.stream.Collectors;

@Scope(value = "session")
@Component(value = "postController")
@ELBeanName(value = "postController")
@Getter
@Setter
public class PostController {

    private final PostRestService postService;

    private final CategoryRestService categoryRestService;

    private final TagRestService tagRestService;

    private final CategoryComponent categoryComponent;

    private final MediaComponent mediaComponent;

    private InputText inputTextTitle;
    private InputText inputTextSlug;

    private PostDTO post;
    private List<PostDTO> selectedPosts;
    private GenericLazyDataModel<PostDTO> posts;
    private TreeNode[] selectedNodes;

    public PostController(PostRestService postService,
                          CategoryRestService categoryRestService,
                          TagRestService tagRestService,
                          CategoryComponent categoryComponent,
                          MediaComponent mediaComponent) {
        this.postService = postService;
        this.categoryRestService = categoryRestService;
        this.tagRestService = tagRestService;
        this.categoryComponent = categoryComponent;
        this.mediaComponent = mediaComponent;
    }

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

        if (StringUtils.isBlank(post.getPostTitle())) {
            FacesContextUtil.addMessageError("Title must not be null");
            return false;
        }

        if (selectedNodes == null || selectedNodes.length <= 0) {
            FacesContextUtil.addMessageError("Select at least one category");
            return false;
        }
        return true;
    }

    public void preRenderIndex() {
        selectedPosts = new ArrayList<>();
    }

    public void preRenderView() {
        if (FacesContextUtil.isNotPostback()) {
            //add new
            Optional<String> status = FacesContextUtil.getRequestParam("status");
            if (status.isPresent() && status.get().equalsIgnoreCase("new")) {
                post = new PostDTO();

                editPost(post);

                if (inputTextTitle != null) {
                    inputTextTitle.setValue("");
                }

                if (inputTextSlug != null) {
                    inputTextSlug.setValue("");
                }
                return;
            }
            //edit
            Long postId = FacesContextUtil.getRequestParamAsLong("postId");

            if (postId == null) FacesContextUtil.redirect("/");

            ObjectResult<PostDTO> p = postService.findById(postId);
            if (p.isPresent()) {
                post = p.getResult();

                editPost(post);

                if (inputTextTitle != null) {
                    inputTextTitle.setValue(post.getPostTitle());
                }

                if (inputTextSlug != null) {
                    inputTextSlug.setValue(post.getPostSlug());
                }
            }
        }
    }

    public void add() {

        if (!validateData()) return;

        Long id = post.getId();

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

        if (StringUtils.isBlank(post.getSlug())) {
            updateSlug(post);
        }

        post = postService.save(ObjectRequest.of(post)).getResult();

        FacesContextUtil.addMessageInfo(id);

        if (id == null) {
            FacesContextUtil.redirect("/post/edit/" + post.getId());
        }
    }

    public void createSlug() {

        if (inputTextTitle == null || StringUtils.isBlank(inputTextTitle.getValue().toString())) return;

        if (inputTextTitle.getValue() != null && StringUtils.isBlank(post.getPostTitle())) {
            post.setPostTitle(inputTextTitle.getValue().toString());
        }

        if (inputTextTitle.getValue() != null
                && !post.getPostTitle().equalsIgnoreCase(inputTextTitle.getValue().toString())) {
            post.setPostTitle(inputTextTitle.getValue().toString());
        }

        updateSlug(post, inputTextSlug.getValue() != null ? inputTextSlug.getValue().toString() : "");

        inputTextSlug.setValue(post.getSlug());
    }

    public void deletePost(Long id) {
        postService.delete(id);
        initData();
    }

    public void deletePosts() {
        if (CollectionUtils.isEmpty(selectedPosts)) {
            selectedPosts = Collections.emptyList();
        }

        selectedPosts.forEach(item -> postService.delete(item.getId()));

        posts = loadData();

        selectedPosts = Collections.emptyList(); //reset
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
        TreeNode root = categoryComponent.getRoot();
        checkRoot(cat, root);

        List<TreeNode> nodeList = new ArrayList<>();
        updateSelectedNodes(nodeList, root);

        TreeNode[] nodesSelected = new TreeNode[nodeList.size()];
        nodeList.toArray(nodesSelected);

        selectedNodes = nodesSelected;
    }

    public List<String> completeTag(String query) {

        List<TagDTO> tagsSearchResult = tagRestService.complete(query, 10).getResult();

        if (CollectionUtils.isEmpty(tagsSearchResult)) return Collections.emptyList();

        List<String> tagsName = tagsSearchResult.stream().map(TermDTO::getName).collect(Collectors.toList());

        List<TagDTO> tagsPost = post.getTags();
        if (CollectionUtils.isEmpty(tagsPost)) return tagsName;

        List<String> tagsPostName = tagsPost.stream().map(TermDTO::getName).collect(Collectors.toList());
        tagsName.removeAll(tagsPostName);//remove duplicate tag name

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

                TagDTO tag = tagRestService.findByName(tagSelect, null).getResult();
                if (tag != null) {
                    getPost().getTags().add(tag);
                }
            }
        }
    }

    public void initFeaturedImage() {
        mediaComponent.initData();
    }

    public void chooseImage() {
        Optional<String> imageUrl = FacesContextUtil.getRequestParam("imageUrl");
        imageUrl.ifPresent(s -> post.getAttributes().put(PostMeta.featuredImage, s));
    }

    public void removeFeaturedImage() {
        post.setFeaturedImage("");
    }

    //<editor-fold desc="private">
    private void editPost(PostDTO post) {
        Set<CategoryDTO> cat = post.getCategories();
        categoryComponent.initData();
        if (CollectionUtils.isNotEmpty(cat)) {
            TreeNode root = categoryComponent.getRoot();
            checkRoot(cat, root);
        }
    }

    private void updateSlug(PostDTO post) {

        if (post == null || StringUtils.isBlank(post.getName())) return;

        if (StringUtils.isBlank(post.getSlug())) {
            String slug = postService.makeSlug(post.getId(), post.getName());
            post.setSlug(slug);
        }
    }

    private void updateSlug(PostDTO node, String nameOfSlug) {

        if (node == null || StringUtils.isBlank(node.getName())) return;

        if (StringUtils.isNotBlank(node.getName()) && StringUtils.isBlank(nameOfSlug)) {
            String slug = postService.makeSlug(node.getId(), node.getName());
            node.setSlug(slug);
        }
    }

    private void checkRoot(Set<CategoryDTO> sub, TreeNode node) {
        List<TreeNode> child = node.getChildren();

        if (CollectionUtils.isEmpty(child)) return;

        child.forEach(c -> {
            sub.forEach(s -> {
                CategoryDTO cat = (CategoryDTO) c.getData();
                Long categoryId = s.getCategoryId();
                Long categoryId1 = cat.getCategoryId();
                if (categoryId != null && categoryId1 != null) {
                    if (categoryId.equals(categoryId1)) {
                        c.setSelected(true);
                    }
                }
            });
            if (CollectionUtils.isNotEmpty(c.getChildren())) {
                checkRoot(sub, c);//recursion
            }
        });
    }

    private GenericLazyDataModel<PostDTO> loadData() {

        return new GenericLazyDataModel<>() {
            @Override
            public List<PostDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

                PagedRequest pageRequest = FacesContextUtil.getListPageRequest(first, pageSize, sortBy, filterBy);

                PostRequestDTO param = new PostRequestDTO();
                param.setCateQuery(FacesContextUtil.getRequestParam("cat").orElse(""));
                param.setTagQuery(FacesContextUtil.getRequestParam("tag").orElse(""));

                PagedResult<PostDTO> result = postService.findAll(PageableRequest.of(param, pageRequest));

                setRowCount((int) result.getTotalElements());

                return result.getPagedList();
            }
        };
    }

    private void updateSelectedNodes(List<TreeNode> nodeList, TreeNode root) {

        List<TreeNode> childOfRoot = root.getChildren().stream()
                .filter(TreeNode::isSelected)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(childOfRoot)) {
            nodeList.addAll(childOfRoot);
        }

        root.getChildren().forEach(c -> {
            if (CollectionUtils.isNotEmpty(c.getChildren())) {
                updateSelectedNodes(nodeList, c);//recursion
            }
        });
    }
    //</editor-fold>
}