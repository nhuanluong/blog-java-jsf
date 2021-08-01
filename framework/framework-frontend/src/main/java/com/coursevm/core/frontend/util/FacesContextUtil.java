/*
 * Created on 2021.07.15 (y.M.d) 08:38
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.frontend.util;

import com.coursevm.core.dto.request.PagedRequest;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.PF;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Nhuan Luong
 */
@UtilityClass
public class FacesContextUtil {

    private ServletRequest getServletRequest(FacesContext context) {
        Map<String, Object> requests = context.getExternalContext().getRequestMap();
        for (String requestName : requests.keySet()) {
            if (requests.get(requestName) instanceof HttpServletRequestWrapper) {
                return ((HttpServletRequestWrapper) requests.get(requestName)).getRequest();
            }
        }
        return null;
    }

    public Long getRequestParamAsLong(String key) {

        Optional<String> id = getRequestParam(key);

        if (id.isPresent() && StringUtils.isNumeric(id.get())) return Long.parseLong(id.get());

        return null;
    }

    public Optional<String> getRequestParam(String key) {
        String parameter = Faces.getRequestParameter(key);//fc.getExternalContext().getRequestParameterMap().get(key);
        ServletRequest portalRequest = getServletRequest(Faces.getContext());
        if (StringUtils.isBlank(parameter) && portalRequest != null) {
            Map<String, String[]> originalParams = portalRequest.getParameterMap();
            if (originalParams.get(key) != null) {
                parameter = originalParams.get(key)[0];
            }
        }
        return Optional.ofNullable(StringUtils.trimToNull(parameter));
    }


    public void addMessageInfo(String messageSummary) {
        FacesMessage fm = new FacesMessage(messageSummary);
        FacesContext.getCurrentInstance().addMessage("", fm);
    }

    public void addMessageInfo(Long id) {
        if (id == null) {
            addMessageInfo("Added");
        } else {
            addMessageInfo("Updated");
        }
    }

    public void addMessageError(String messageSummary) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSummary, "");
        FacesContext.getCurrentInstance().addMessage("", fm);
    }

    public void addMessageInfo(String messageDetail, String summary) {
        FacesMessage fm = new FacesMessage();
        fm.setSummary(summary);
        fm.setDetail(messageDetail);
        FacesContext.getCurrentInstance().addMessage("", fm);
    }

    public ConfigurableNavigationHandler getNavigationHandler() {
        return (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    }

    public void performNavigation(String viewPath) {
        performNavigation(viewPath, true);
    }

    public void performNavigation(String viewPath, boolean isRedirect) {
        int paramMask = viewPath.indexOf("?");
        if (paramMask == -1 && isRedirect) {
            viewPath += "?faces-redirect=true";
        } else {
            viewPath += "&faces-redirect=true";
        }
        getNavigationHandler().performNavigation(viewPath);
    }

    public PagedRequest getListPageRequest(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        Sort sort = null;
        PagedRequest pageRequest;

        if (sortBy != null) {
            for (String key : sortBy.keySet()) {

                if (key.contains("DESCENDING") || key.contains("ASCENDING")) continue;

                Collection<SortMeta> vv = sortBy.values();

                for (SortMeta meta : vv) {
                    SortOrder or = meta.getOrder();

                    if (sort == null) {
                        if (or.isAscending()) sort = Sort.by(Sort.Direction.ASC, key);
                        if (or.isDescending()) sort = Sort.by(Sort.Direction.DESC, key);
                        continue;
                    }

                    if (or.isAscending()) sort.and(Sort.by(Sort.Direction.ASC, key));
                    if (or.isDescending()) sort.and(Sort.by(Sort.Direction.DESC, key));
                }
            }
        }

        pageSize = pageSize > 0 ? pageSize : 10;

        if (sort != null) {
            pageRequest = PagedRequest.of(first / pageSize, pageSize, sort);
        } else {
            pageRequest = PagedRequest.of(first / pageSize, pageSize);
        }
        return pageRequest;
    }

    @SneakyThrows
    public void redirect(String url) {
        ExternalContext externalContext = Faces.getExternalContext();
        externalContext.getFlash().setKeepMessages(true);
        externalContext.redirect(Faces.getRequestContextPath() + url);
    }

    public boolean isPostback() {
        return Faces.isPostback();
    }

    public boolean isNotPostback() {
        return !isPostback();
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        Faces.getContext().addMessage(null, new FacesMessage(severity, summary, detail));
        Ajax.update("growl");
    }

    public void showInfo(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, "Info", message);
    }

    public void showWarn(String message) {
        addMessage(FacesMessage.SEVERITY_WARN, "Warn", message);
    }

    public void showError(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, "Error", message);
        Faces.validationFailed();
    }
}
