///*
// * Created on 2021.08.01 (y.M.d) 23:18
// *
// * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
// * This software is the proprietary information of VietInfo Company.
// *
// */
//package com.coursevm.web.blog.validator;
//
//import com.coursevm.core.frontend.util.FacesContextUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.omnifaces.util.Faces;
//import org.omnifaces.util.Messages;
//
//import javax.faces.component.UIComponent;
//import javax.faces.context.FacesContext;
//import javax.faces.validator.FacesValidator;
//import javax.faces.validator.Validator;
//import javax.faces.validator.ValidatorException;
//
///**
// * @author Nhuan Luong
// */
//@FacesValidator("titleValidator")
//public class TitleValidator implements Validator {
//    @Override
//    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//        if (value == null || StringUtils.isBlank(value.toString())) {
//            FacesContextUtil.showError("Title must not be null");
//            //Messages.addError(component.getClientId(context), "Validator currently used: {0}", this);
//        }
//    }
//}
