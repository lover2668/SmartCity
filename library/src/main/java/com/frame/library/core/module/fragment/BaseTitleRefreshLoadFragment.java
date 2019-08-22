package com.frame.library.core.module.fragment;

import android.os.Bundle;

import com.frame.library.core.control.IFrameTitleView;
import com.frame.library.core.delegate.FrameTitleDelegate;
import com.frame.library.core.widget.titlebar.TitleBarView;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:34
 * @E-Mail: 971613168@qq.com
 * Function: 设置有TitleBar及下拉刷新Fragment
 * Description:
 */
public abstract class BaseTitleRefreshLoadFragment<T> extends BaseRefreshLoadFragment<T> implements IFrameTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        mTitleBar = new FrameTitleDelegate(mContentView, this, getClass()).mTitleBar;
        super.beforeInitView(savedInstanceState);
    }
}
