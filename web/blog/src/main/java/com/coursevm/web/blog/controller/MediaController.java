package com.coursevm.web.blog.controller;

import com.coursevm.backend.blog.dto.MediaDTO;
import com.coursevm.backend.blog.rest.MediaRestService;
import com.coursevm.core.common.util.TextUtil;
import com.coursevm.core.dto.request.ObjectRequest;
import com.coursevm.core.dto.request.PageableRequest;
import com.coursevm.core.dto.request.PagedRequest;
import com.coursevm.core.dto.response.ObjectResult;
import com.coursevm.core.dto.response.PagedResult;
import com.coursevm.core.frontend.primefaces.GenericLazyDataModel;
import com.coursevm.core.frontend.util.FacesContextUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;

@Scope(value = "session")
@Component(value = "mediaController")
@ELBeanName(value = "mediaController")
@Getter
@Setter
public class MediaController {

    @Autowired
    private MediaRestService mediaRestService;

    private MediaDTO media;
    private List<MediaDTO> selectedMedia;
    private GenericLazyDataModel<MediaDTO> lstMedia;

    @PostConstruct
    public void initData() {
        media = new MediaDTO();
        lstMedia = loadData();
    }

    public boolean validateData() {
        if (media == null) {
            FacesContextUtil.addMessageError("Media needs be init data");
            return false;
        }
        return true;
    }

    public void preRenderView() {
        if (FacesContextUtil.isNotPostback()) {
            //add new
            Optional<String> status = FacesContextUtil.getRequestParam("status");
            if (status.isPresent() && status.get().equalsIgnoreCase("new")) {
                media = new MediaDTO();

                return;
            }
            //edit
            Long postId = FacesContextUtil.getRequestParamAsLong("mediaId");

            if (postId == null) FacesContextUtil.redirect("/");

            ObjectResult<MediaDTO> p = mediaRestService.findById(postId);
            if (p.isPresent()) {
                media = p.getResult();
            }
            //isEditImage = false;
            imageOriginal = media.getPostGUID();
        }
    }

    public void add() {

        Long id = null;

        if (!validateData()) return;

        if (media.getId() != null) {
            id = media.getId();
        }

        if (StringUtils.isBlank(media.getSlug())) updateSlug(media);

        media = mediaRestService.save(ObjectRequest.of(media)).getResult();

        FacesContextUtil.addMessageInfo(id);

        if (id == null) {
            FacesContextUtil.redirect("/media/edit/" + media.getId());
        }
    }

    private void updateSlug(MediaDTO node) {

        if (node == null || StringUtils.isBlank(node.getName())) return;

        if (StringUtils.isNotBlank(node.getSlug())) {
            node.setSlug(TextUtil.makeUrlFriendly(node.getSlug()));
        }
    }

    public void deleteMedia(Long id) {
        mediaRestService.delete(id);
        initData();
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

    @SneakyThrows
    public void upload(FileUploadEvent uploadEvent) {
        UploadedFile eventFile = uploadEvent.getFile();
        String path = Faces.getRealPath("/");
        String fileName = TextUtil.makeFileName(eventFile.getFileName());
        String relatePath = File.separator + "uploads" + File.separator + fileName;
        String uploadPath = path + relatePath;
        File file = new File(uploadPath);
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            byte[] strToBytes = eventFile.getContent();
            outputStream.write(strToBytes);
        }

        MediaDTO m = MediaDTO.builder()
                .postTitle(fileName)
                .postMimeType(eventFile.getContentType())
                .postGUID(relatePath)
                .build();
        mediaRestService.save(ObjectRequest.of(m));
    }


    public String getPath(String image) {
        ExternalContext externalContext = Faces.getExternalContext();
        String hostname = externalContext.getRequestServerName();
        int port = externalContext.getRequestServerPort();
        String schema = externalContext.getRequestScheme();

        return schema + "://" + hostname + ":" + port + Faces.getRequestContextPath() + image;
    }

    private Boolean isEditImage = false;
    private String imageOriginal;

    public void switchEdit() {
        if (isEditImage == null) isEditImage = false;
        isEditImage = !isEditImage;
    }

    private CroppedImage croppedImage;

    @SneakyThrows
    public void crop() {
        String path = Faces.getRealPath("/");
        String fileName = TextUtil.makeFileName(media.getPostTitle());
        String relatePath = File.separator + "uploads" + File.separator + "crops" + File.separator + "crop-" +fileName;
        String uploadPath = path + relatePath;
        File file = new File(uploadPath);
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            byte[] strToBytes = croppedImage.getBytes();
            outputStream.write(strToBytes);
        }
        media.setPostGUID(relatePath);
    }

    public void restoreImage() {
        media.setPostGUID(imageOriginal);
    }

    public void selectionImageChange(ImageAreaSelectEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Area selected",
                "X1: " + e.getX1() + ", X2: " + e.getX2() + ", Y1: " + e.getY1() + ", Y2: " + e.getY2()
                        + ", Image width: " + e.getImgWidth() + ", Image height: " + e.getImgHeight());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void rotateListener(RotateEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Image rotated",
                "Degree:" + e.getDegree());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void resizeListener(ResizeEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Image resized",
                "Width:" + e.getWidth() + ", Height: " + e.getHeight());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public Integer viewMode = 0;
    public void viewModeImageEditor() {
        viewMode = viewMode++;
        if (viewMode > 3) viewMode = 0;
    }
}