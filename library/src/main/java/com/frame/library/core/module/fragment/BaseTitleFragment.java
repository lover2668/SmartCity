package com.frame.library.core.module.fragment;

import android.os.Bundle;

import com.frame.library.core.FrameLifecycleCallbacks;
import com.frame.library.core.basis.BaseFragment;
import com.frame.library.core.control.IFrameTitleView;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.title.TitleBarView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:34
 * @E-Mail: 971613168@qq.com
 * Function: 设置有TitleBar的Fragment
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link IFrameTitleView}通过接口方式由FastLib自动处理{@link FrameLifecycleCallbacks#onFragmentStarted(FragmentManager, Fragment)}
 */
public abstract class BaseTitleFragment extends BaseFragment implements IFrameTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }
}
