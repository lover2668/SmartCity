package com.tourcool.core.util;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.frame.library.core.util.FormatUtil;
import com.tourcool.core.MyApplication;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.RouteConstance;

import java.text.DecimalFormat;

/**
 * @author :JenkinsZhou
 * @description :工具类
 * @company :途酷科技
 * @date 2019年08月20日10:27
 * @Email: 971613168@qq.com
 */
public class TourCooUtil {
    private static final String STRING_LINE = "/";
    private static final String STRING_EMPTY = "";
    private static final String URL_TAG = "http";
    private static final String URL_TAG_HTTPS = "https";
    private static final int LENGTH_PHONE = 11;

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


    public static Drawable getDrawable(int drawableId) {
        return ContextCompat.getDrawable(MyApplication.getInstance(), drawableId);
    }


    public static String getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return STRING_EMPTY;
        }
        if (url.contains(URL_TAG) || url.contains(URL_TAG_HTTPS)) {
            return url;
        } else {
            if (url.startsWith(STRING_LINE)) {
                return RequestConfig.BASE_URL_NO_LINE + url;
            } else {
                return RequestConfig.BASE_URL + url;
            }
        }
    }


    public static boolean isMobileNumber(String mobileNums) {
        if (TextUtils.isEmpty(mobileNums)) {
            return false;
        } else {
            String startValue = "1";
            return mobileNums.length() == LENGTH_PHONE && mobileNums.startsWith(startValue);
        }
    }



    public static String doubleTransStringZhen(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        //四舍五入 并保留两位小数
        double value = Double.parseDouble(FormatUtil.formatDoubleSize(d, 2));
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(value);
    }


}
