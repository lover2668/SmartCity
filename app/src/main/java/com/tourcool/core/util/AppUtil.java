package com.tourcool.core.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:29
 * @E-Mail: 971613168@qq.com
 * @Function: app工具类
 * @Description:
 */
public class AppUtil {

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
