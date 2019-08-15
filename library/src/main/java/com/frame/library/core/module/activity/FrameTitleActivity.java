package com.frame.library.core.module.activity;

import android.app.Activity;
import android.os.Bundle;

import com.frame.library.core.FrameLifecycleCallbacks;
import com.frame.library.core.control.IFrameTitleView;
import com.frame.library.core.basis.BaseActivity;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:35
 * @E-Mail: 971613168@qq.com
 * Function: 包含TitleBarView的Activity
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link IFrameTitleView}通过接口方式由FastLib自动处理{@link FrameLifecycleCallbacks#onActivityStarted(Activity)}
 */
public abstract class FrameTitleActivity extends BaseActivity implements IFrameTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }

    @Override
    protected void onDestroy() {
        mTitleBar = null;
        super.onDestroy();
    }
}
