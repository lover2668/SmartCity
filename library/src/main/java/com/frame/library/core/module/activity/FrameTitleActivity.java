package com.frame.library.core.module.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.FrameLifecycleCallbacks;
import com.frame.library.core.control.IFrameTitleView;
import com.frame.library.core.basis.BaseActivity;
import com.aries.ui.util.FindViewUtil;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:35
 * @E-Mail: 971613168@qq.com
 * Function: 包含TitleBarView的Activity
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link IFrameTitleView}通过接口方式由FastLib自动处理{@link FrameLifecycleCallbacks#onActivityStarted(Activity)}
 */
public abstract class FrameTitleActivity extends BaseActivity implements IFrameTitleView {
    protected int statusBarHeight;
    protected TitleBarView mTitleBar;

    protected Handler mHandler = new Handler();

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        statusBarHeight = StatusBarUtil.getStatusBarHeight();
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
        if (mTitleBar != null) {
            mTitleBar.setTextBoldMode(isTitleBold());
        }
    }

    @Override
    protected void onDestroy() {
        mTitleBar = null;
        super.onDestroy();
    }


    protected void setClickEnable(View view, boolean clickEnable) {
        if (view == null) {
            TourCooLogUtil.e(TAG, "setViewClickEnable() ----> textView或 字符串为空");
            return;
        }
        view.setEnabled(clickEnable);
    }

    protected void setText(TextView textView, String text) {
        if (textView == null || TextUtils.isEmpty(text)) {
            TourCooLogUtil.e(TAG, "setText() ----> textView或 字符串为空");
            return;
        }
        textView.setText(text);
    }

}
