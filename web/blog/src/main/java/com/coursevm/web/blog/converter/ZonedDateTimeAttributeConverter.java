///*
// * Created on 2021.07.31 (y.M.d) 12:56
// *
// * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
// * This software is the proprietary information of VietInfo Company.
// *
// */
//package com.coursevm.web.blog.converter;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.time.ZonedDateTime;
//import java.util.Calendar;
//import java.util.TimeZone;
//
///**
// * @author Nhuan Luong
// */
//@Converter(autoApply = true)
//public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Calendar> {
//
//    @Override
//    public Calendar convertToDatabaseColumn(ZonedDateTime entityAttribute) {
//        if (entityAttribute == null) {
//            return null;
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(entityAttribute.toInstant().toEpochMilli());
//        calendar.setTimeZone(TimeZone.getTimeZone(entityAttribute.getZone()));
//        return calendar;
//    }
//
//    @Override
//    public ZonedDateTime convertToEntityAttribute(Calendar databaseColumn) {
//        if (databaseColumn == null) {
//            return null;
//        }
//
//        return ZonedDateTime.ofInstant(databaseColumn.toInstant(), databaseColumn.getTimeZone().toZoneId());
//    }
//
//}
