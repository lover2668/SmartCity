package com.tourcool.core.helper;

import androidx.core.content.ContextCompat;

import com.tourcool.core.MyApplication;
import com.aries.ui.view.radius.delegate.RadiusViewDelegate;
import com.tourcool.library.frame.R;

/**
 * @Author: JenkinsZhou on 2018/9/30 9:16
 * @E-Mail: 971613168@qq.com
 * @Function: 圆角View帮助类
 * @Description:
 */
public class RadiusViewHelper {

    private static volatile RadiusViewHelper sInstance;

    private RadiusViewHelper() {
    }

    public static RadiusViewHelper getInstance() {
        if (sInstance == null) {
            synchronized (RadiusViewHelper.class) {
                if (sInstance == null) {
                    sInstance = new RadiusViewHelper();
                }
            }
        }
        return sInstance;
    }

    public void setRadiusViewAdapter(RadiusViewDelegate delegate) {
        if (!MyApplication.isSupportElevation()) {
            delegate.setStrokeWidth(MyApplication.getContext().getResources().getDimensionPixelSize(R.dimen.dp_line_size))
                    .setStrokeColor(ContextCompat.getColor(MyApplication.getContext(), R.color.colorLineGray))
                    .init();
        }
    }
}
