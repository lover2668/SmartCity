package com.frame.library.core.delegate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frame.library.core.UiManager;
import com.frame.library.core.control.IFrameRefreshView;
import com.aries.library.fast.R;
import com.frame.library.core.manager.LoggerManager;
import com.aries.ui.util.FindViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * @Author: JenkinsZhou on 2019/3/22 14:18
 * @E-Mail: 971613168@qq.com
 * @Function: 快速实现下拉刷新布局代理
 * @Description:
 */
public class FrameRefreshDelegate {

    public SmartRefreshLayout mRefreshLayout;
    public View mRootView;
    private UiManager mManager;
    private IFrameRefreshView mIFrameRefreshView;
    private Context mContext;

    public FrameRefreshDelegate(View rootView, IFrameRefreshView iFrameRefreshView) {
        this.mRootView = rootView;
        this.mIFrameRefreshView = iFrameRefreshView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = UiManager.getInstance();
        if (mIFrameRefreshView == null) {
            return;
        }
        if (mRootView == null) {
            mRootView = mIFrameRefreshView.getContentView();
        }
        if (mRootView == null) {
            return;
        }
        getRefreshLayout(rootView);
        initRefreshHeader();
        if (mRefreshLayout != null) {
            mIFrameRefreshView.setRefreshLayout(mRefreshLayout);
        }
    }

    /**
     * 初始化刷新头配置
     */
    protected void initRefreshHeader() {
        if (mRefreshLayout == null) {
            return;
        }
        if (mRefreshLayout.getRefreshHeader() != null) {
            return;
        }
        mRefreshLayout.setRefreshHeader(mIFrameRefreshView.getRefreshHeader() != null
                ? mIFrameRefreshView.getRefreshHeader() :
                mManager.getDefaultRefreshHeader() != null ?
                        mManager.getDefaultRefreshHeader().createRefreshHeader(mContext, mRefreshLayout) :
                        new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(mIFrameRefreshView);
        mRefreshLayout.setEnableRefresh(mIFrameRefreshView.isRefreshEnable());
    }

    /**
     * 获取布局里的刷新Layout
     *
     * @param rootView
     * @return
     */
    private void getRefreshLayout(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.smartLayout_rootFastLib);
        if (mRefreshLayout == null) {
            mRefreshLayout = FindViewUtil.getTargetView(rootView, SmartRefreshLayout.class);
        }
        //原布局无SmartRefreshLayout 将rootView 从父布局移除并添加进SmartRefreshLayout 将SmartRefreshLayout作为新的
        if (mRefreshLayout == null && mIFrameRefreshView.isRefreshEnable()) {
            ViewGroup parentLayout;
            ViewGroup.LayoutParams params = mRootView.getLayoutParams();

            if (mRootView.getParent() != null) {
                parentLayout = (ViewGroup) mRootView.getParent();
            } else {
                parentLayout = mRootView.getRootView().findViewById(android.R.id.content);
            }
            //如果此时parentLayout为null 可能mRootView为Fragment 根布局
            if (parentLayout == null) {
                return;
            }
            int index = parentLayout.indexOfChild(mRootView);
            //先移除rootView
            parentLayout.removeView(mRootView);
            //新建SmartRefreshLayout
            mRefreshLayout = new SmartRefreshLayout(mRootView.getContext());
            //将rootView添加进SmartRefreshLayout
            mRefreshLayout.addView(mRootView);
            //将SmartRefreshLayout添加进parentLayout
            parentLayout.addView(mRefreshLayout, index, params);
        }
    }


    /**
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        mRefreshLayout = null;
        mContext = null;
        mManager = null;
        mRootView = null;
        LoggerManager.i("FrameRefreshDelegate", "onDestroy");
    }
}
