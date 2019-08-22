package com.frame.library.core.module.activity;

import android.os.Bundle;

import com.frame.library.core.basis.BaseActivity;
import com.frame.library.core.control.IFrameMainView;
import com.frame.library.core.delegate.FrameMainTabDelegate;
import com.tourcool.library.frame.demo.R;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:00
 * @E-Mail: 971613168@qq.com
 * Function: 快速创建主页Activity布局
 * Description:
 */
public abstract class FrameMainActivity extends BaseActivity implements IFrameMainView {

    protected FrameMainTabDelegate mFrameMainTabDelegate;

    @Override
    public int getContentLayout() {
        return isSwipeEnable() ? R.layout.frame_activity_main_view_pager : R.layout.frame_activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mFrameMainTabDelegate != null) {
            mFrameMainTabDelegate.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mFrameMainTabDelegate = new FrameMainTabDelegate(mContentView, this, this);
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    @Override
    public void onBackPressed() {
        quitApp();
    }

    @Override
    protected void onDestroy() {
        if (mFrameMainTabDelegate != null) {
            mFrameMainTabDelegate.onDestroy();
        }
        super.onDestroy();
    }
}
