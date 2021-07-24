/*
 * Created on 2020.12.07 (y.M.d) 11:34
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.common.util;

import com.github.slugify.Slugify;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Nhuan Luong
 */
@UtilityClass
public class TextUtil {

    private static final Slugify slug = new Slugify();

    public String joiningName(String... strings) {
        return joiningWithDelimiter(" ", strings);
    }

    public String joiningWithDelimiter(String delimiter, String... strings) {
        return Arrays.stream(strings)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(delimiter));
    }

    public String makeFileName(String fname) {

        String fileName = StringUtils.trim(removeUnicode(fname));
        String extension = "." + FilenameUtils.getExtension(fname.toLowerCase());
        String fileBase = fileName.substring(0, fileName.lastIndexOf("."));

        String fileNameResult = slug
                .withLowerCase(true)
                .withUnderscoreSeparator(true)
                .slugify(fileBase.replace("-", "_"));

        if (fileNameResult.length() > 1) {
            fileNameResult = fileNameResult.startsWith("_") ? fileNameResult.substring(1) : fileNameResult;
            fileNameResult = fileNameResult.endsWith("_") ? fileNameResult.substring(0, fileNameResult.length() - 1) : fileNameResult;
        }
        return (fileNameResult.length() > 100 ? fileNameResult.substring(0, 100) : fileNameResult) + extension;
    }

    public String makeUrlFriendly(String url) {

        String fileName = StringUtils.trim(removeUnicode(url));

        String fileNameResult = slug
                .withLowerCase(true)
                .withUnderscoreSeparator(false)
                .slugify(fileName);

        if (fileNameResult.length() > 1) {
            fileNameResult = fileNameResult.startsWith("_") ? fileNameResult.substring(1) : fileNameResult;
            fileNameResult = fileNameResult.endsWith("_") ? fileNameResult.substring(0, fileNameResult.length() - 1) : fileNameResult;
        }
        return fileNameResult;
    }

    public static void main(String[] args) {
        String s = "Số 1657/SXD-QLCL ngày 01-02-2019 - - - - --  -- ------ .Trích yếu v/v thông báo kết quả kiểm tra công tác nghiệm thu khi hoàn thành thi công xây dựng công trình cải tạo nhà giữ xe, xây dựng tại 59.pdf -----  --";
        System.out.println("removeUnicode:" + makeUrlFriendly(s));
    }

    private static String removeUnicode(String title) {

        if (StringUtils.isBlank(title)) return title;

        title = StringUtils.lowerCase(title);

        var a = new char[] { 'à', 'á', 'ạ', 'ả', 'ã', 'â', 'ầ', 'ấ', 'ậ', 'ẩ', 'ẫ', 'ă', 'ằ', 'ắ', 'ặ', 'ẳ', 'ẵ'};
        title = clean('a', title, a);

        var e = new char[] { 'è', 'é', 'ẹ', 'ẻ', 'ẽ', 'ê', 'ề', 'ế', 'ệ', 'ể', 'ễ'};
        title = clean('e', title, e);

        var o = new char[] { 'ò', 'ó', 'ọ', 'ỏ', 'õ', 'ô', 'ồ', 'ố', 'ộ', 'ổ', 'ỗ', 'ơ', 'ờ', 'ớ', 'ợ', 'ở', 'ỡ' };
        title = clean('o', title, o);

        var i = new char[] { 'ì', 'í', 'ị', 'ỉ', 'ĩ'};
        title = clean('i', title, i);

        var u = new char[] { 'ù', 'ú', 'ụ', 'ủ', 'ũ', 'ư', 'ừ', 'ứ', 'ự', 'ử', 'ữ'};
        title = clean('u', title, u);

        var y = new char[] { 'ỳ', 'ý', 'ỵ', 'ỷ', 'ỹ' };
        title = clean('y', title, y);

        title = title.replace('đ', 'd');

        /*title = title
                .replaceAll("[\\s-]+", " ")
                .replaceAll("[\\s]", "_");*/

        return title;
    }

    private static String clean(char replace, String title, char[] a) {

        if (StringUtils.isBlank(title)) return title;

        for (char c : a) {
            title = title.replace(c, replace);
        }
        return title;
    }

    private static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}
