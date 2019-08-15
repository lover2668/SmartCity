package com.tourcool.core.module.main.sample;

import android.graphics.Color;
import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.aries.ui.view.title.TitleBarView;
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
    }
}
