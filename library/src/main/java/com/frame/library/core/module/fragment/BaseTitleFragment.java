package com.frame.library.core.module.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.FrameLifecycleCallbacks;
import com.frame.library.core.basis.BaseFragment;
import com.frame.library.core.control.IFrameTitleView;
import com.aries.ui.util.FindViewUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.library.frame.demo.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:34
 * @E-Mail: 971613168@qq.com
 * Function: 设置有TitleBar的Fragment
 * Description:
 * 1、2019-3-25 17:03:43 推荐使用{@link IFrameTitleView}通过接口方式由FrameLib自动处理{@link FrameLifecycleCallbacks#onFragmentStarted(FragmentManager, Fragment)}
 */
public abstract class BaseTitleFragment extends BaseFragment implements IFrameTitleView {

    protected TitleBarView mTitleBar;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mTitleBar = FindViewUtil.getTargetView(mContentView, TitleBarView.class);
    }


    /**
     * 让布局下移到状态栏以下
     *
     * @param containerView
     */
    protected void setViewToBelowStatusBar(View containerView) {
        if (containerView == null) {
            return;
        }
        if (containerView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) containerView.getLayoutParams();
            params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
            containerView.setLayoutParams(params);
        } else if (containerView.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) containerView.getLayoutParams();
            params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
            containerView.setLayoutParams(params);
        } else if (containerView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) containerView.getLayoutParams();
            params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
            containerView.setLayoutParams(params);
        }


    }

}
