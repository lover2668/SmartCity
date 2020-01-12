package com.tourcool.core.module.main.sample;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.smartcity.R;

/**
 * @Author: JenkinsZhou on 2018/9/19 10:37
 * @E-Mail: 971613168@qq.com
 * Function: QQ默认主题Title背景渐变
 * Description:
 */
public class QQTitleActivity extends FrameTitleActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_qq_title;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftTextDrawable(R.drawable.ic_back_white)
                .setStatusBarLightMode(false)
                .setTitleMainTextColor(Color.WHITE)
                .setBgResource(R.drawable.shape_qq_bg);
        titleBar.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = titleBar.getLeftDrawable();
                TourCooLogUtil.i(TAG, "宽高-------->" + SizeUtil.px2dp(drawable.getIntrinsicHeight()));
                TourCooLogUtil.i(TAG, "宽高:" + drawable.getIntrinsicHeight() + "----" + titleBar.getLeftTextDrawableWidth());
                TourCooLogUtil.i(TAG, "宽高:" + drawable.getIntrinsicWidth() + "----" + titleBar.getLeftTextDrawableWidth());
            }
        });
    }
}
