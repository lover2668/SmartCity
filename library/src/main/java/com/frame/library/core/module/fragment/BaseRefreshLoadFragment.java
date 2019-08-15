package com.frame.library.core.module.fragment;

import android.os.Bundle;

import com.frame.library.core.basis.BaseFragment;
import com.frame.library.core.control.IFrameRefreshLoadView;
import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.delegate.FrameRefreshLoadDelegate;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/20 16:55
 * @E-Mail: 971613168@qq.com
 * Function:下拉刷新及上拉加载更多+多状态切换
 * Description:
 * 1、2018-7-20 16:55:45 设置StatusLayoutManager 目标View
 */
public abstract class BaseRefreshLoadFragment<T>
        extends BaseFragment implements IFrameRefreshLoadView<T> {
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusLayoutManager mStatusManager;
    protected int mDefaultPage = 0;
    protected int mDefaultPageSize = 10;
    private BaseQuickAdapter mQuickAdapter;
    private Class<?> mClass;

    protected FrameRefreshLoadDelegate<T> mFrameRefreshLoadDelegate;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mClass = this.getClass();
        mFrameRefreshLoadDelegate = new FrameRefreshLoadDelegate<>(mContentView, this, mClass);
        mRecyclerView = mFrameRefreshLoadDelegate.mRecyclerView;
        mRefreshLayout = mFrameRefreshLoadDelegate.mRefreshLayout;
        mStatusManager = mFrameRefreshLoadDelegate.mStatusManager;
        mQuickAdapter = mFrameRefreshLoadDelegate.mAdapter;
        mFrameRefreshLoadDelegate.setLoadMore(isLoadMoreEnable());
    }

    @Override
    public IHttpRequestControl getIHttpRequestControl() {
        return new IHttpRequestControl() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return mRefreshLayout;
            }

            @Override
            public BaseQuickAdapter getRecyclerAdapter() {
                return mQuickAdapter;
            }

            @Override
            public StatusLayoutManager getStatusLayoutManager() {
                return mStatusManager;
            }

            @Override
            public int getCurrentPage() {
                return mDefaultPage;
            }

            @Override
            public int getPageSize() {
                return mDefaultPageSize;
            }

            @Override
            public Class<?> getRequestClass() {
                return mClass;
            }
        };
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mDefaultPage = 0;
        loadData(mDefaultPage);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(++mDefaultPage);
    }

    @Override
    public void loadData() {
        loadData(mDefaultPage);
    }

    @Override
    public void onDestroy() {
        if (mFrameRefreshLoadDelegate != null) {
            mFrameRefreshLoadDelegate.onDestroy();
        }
        super.onDestroy();
    }
}
