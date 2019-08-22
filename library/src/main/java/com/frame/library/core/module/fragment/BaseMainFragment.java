package com.frame.library.core.module.fragment;

import android.os.Bundle;

import com.frame.library.core.basis.BaseFragment;
import com.frame.library.core.control.IFrameMainView;
import com.frame.library.core.delegate.FrameMainTabDelegate;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.tourcool.library.frame.demo.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * @Author: JenkinsZhou on 2018/7/23 11:27
 * @E-Mail: 971613168@qq.com
 * Function: 快速创建主页布局
 * Description:
 */
public abstract class BaseMainFragment extends BaseFragment implements IFrameMainView, OnTabSelectListener {

    protected FrameMainTabDelegate mFrameMainTabDelegate;

    @Override
    public void setViewPager(ViewPager mViewPager) {
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public int getContentLayout() {
        return isSwipeEnable() ? R.layout.frame_activity_main_view_pager : R.layout.frame_activity_main;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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
    public void onTabReselect(int position) {

    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }
}
