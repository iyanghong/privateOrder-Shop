package com.clever.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xixi
 * @Date 2023-12-18 10:54
 **/
public class RegularUtil {
    /**
     * 邮件正则
     */
    public static final String REGULAR_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 数字正则
     */
    public static final String REGULAR_NUMBER = "^[0-9]*$";

    /**
     * 手机号正则
     */
    public static final String REGULAR_PHONE = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    /**
     * 普通汉字字母正则
     */
    private static final String CHARACTER_REGULAR = "[一-龥a-zA-Z]";

    /**
     * 验证字符串是否匹配此正则
     *
     * @param regular 正则
     * @param string  字符串
     * @return boolean
     */
    public static boolean isMatching(String regular, String string) {
        Matcher matcher = Pattern.compile(regular).matcher(string);
        return matcher.matches();
    }

    /**
     * 是否包含普通字符
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isContainsCommonCharacter(String str) {
        Matcher matcher = Pattern.compile(CHARACTER_REGULAR).matcher(str);
        return matcher.find();
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{}
     * \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    public static String makeQueryStringAllRegExp(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }
}
