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

    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return number.length() == PHONE_LENGTH && number.startsWith(PHONE_START);
    }
}
