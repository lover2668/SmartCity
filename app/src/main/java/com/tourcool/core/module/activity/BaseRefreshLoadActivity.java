package com.tourcool.core.module.activity;

import android.os.Bundle;

import com.frame.library.core.control.IFrameRefreshLoadView;
import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.delegate.FrameRefreshLoadDelegate;
import com.frame.library.core.delegate.FrameTitleDelegate;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.library.core.module.activity.FrameTitleActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/20 16:54
 * @E-Mail: 971613168@qq.com
 * Function:下拉刷新及上拉加载更多
 * Description:
 * 1、2018-7-9 09:50:59 修正Adapter 错误造成无法展示列表数据BUG
 * 2、2018-7-20 16:54:47 设置StatusLayoutManager 目标View
 * 3、2019-4-19 09:41:28 修改IFastTitleView 逻辑
 */
public abstract class BaseRefreshLoadActivity<T>
        extends FrameTitleActivity implements IFrameRefreshLoadView<T> {
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusLayoutManager mStatusManager;
    private BaseQuickAdapter mQuickAdapter;
    protected int mDefaultPage = 1;
    protected int mDefaultPageSize = 10;

    protected FrameRefreshLoadDelegate<T> mFrameRefreshLoadDelegate;
    protected Class<?> mClass;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mClass = getClass();
        new FrameTitleDelegate(mContentView, this, getClass());
        mFrameRefreshLoadDelegate = new FrameRefreshLoadDelegate<>(mContentView, this, getClass());
        mRecyclerView = mFrameRefreshLoadDelegate.mRecyclerView;
        mRefreshLayout = mFrameRefreshLoadDelegate.mRefreshLayout;
        mStatusManager = mFrameRefreshLoadDelegate.mStatusManager;
        mQuickAdapter = mFrameRefreshLoadDelegate.mAdapter;
    }

    @Override
    public IHttpRequestControl getIHttpRequestControl() {
        IHttpRequestControl requestControl = new IHttpRequestControl() {
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
        return requestControl;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mDefaultPage = 1;
        mFrameRefreshLoadDelegate.setLoadMore(isLoadMoreEnable());
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
    protected void onDestroy() {
        if (mFrameRefreshLoadDelegate != null) {
            mFrameRefreshLoadDelegate.onDestroy();
        }
        super.onDestroy();
    }
}
