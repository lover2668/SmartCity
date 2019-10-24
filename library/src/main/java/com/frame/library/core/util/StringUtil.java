package com.frame.library.core.util;

import android.text.TextUtils;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月25日13:52
 * @Email: 971613168@qq.com
 */
public class StringUtil {
    private static final String PHONE_START = "1";
    private static final int PHONE_LENGTH = 11;
    /**
     * 温度符号
     */
    public static final String SYMBOL_TEMP = "°";
    public static final String LINE_HORIZONTAL = "--";
    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return number.length() == PHONE_LENGTH && number.startsWith(PHONE_START);
    }


    public static String  getNotNullValue(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        return number;
    }




}
