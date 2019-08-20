package com.tourcool.core.util;

import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.tourcool.core.MyApplication;

/**
 * @author :JenkinsZhou
 * @description :工具类
 * @company :途酷科技
 * @date 2019年08月20日10:27
 * @Email: 971613168@qq.com
 */
public class TourCoolUtil  {


    public static int getColor(int colorId) {
        return ContextCompat.getColor(MyApplication.getInstance(), colorId);
    }


    public static String getNotNullValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return value;
    }

    public static String getNotNullValueLine(String value) {
        if (TextUtils.isEmpty(value)) {
            return "--";
        }
        return value;
    }

}
