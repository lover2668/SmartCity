package com.tourcool.ui.base;

import android.os.Bundle;
import android.os.Handler;

import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.library.frame.R;


/**
 * @author :JenkinsZhou
 * @description :普通蓝色背景栏
 * @company :途酷科技
 * @date 2019年09月25日15:02
 * @Email: 971613168@qq.com
 */
public abstract class FrameCommonTitleActivity extends FrameTitleActivity {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (mTitleBar != null) {
            mTitleBar.setBgResource(R.drawable.bg_gradient_title_common);
            mTitleBar.setLeftTextDrawable(R.drawable.ic_back_white);
            mTitleBar.setTitleMainTextColor(FrameUtil.getColor(R.color.whiteCommon));
        }
    }




}
