package com.tourcool.core.module.main.sample.news;

import android.os.Bundle;

import com.aries.library.fast.demo.R;
import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.core.adapter.WidgetAdapter;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.fragment.BaseRefreshLoadFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.trello.rxlifecycle3.android.FragmentEvent;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:20
 * @E-Mail: 971613168@qq.com
 * @Function: 腾讯新闻-刷新
 * @Description:
 */
public class NewsRefreshItemFragment extends BaseRefreshLoadFragment {

    public static NewsRefreshItemFragment newInstance() {
        Bundle args = new Bundle();
        NewsRefreshItemFragment fragment = new NewsRefreshItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.frame_layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return new WidgetAdapter();
    }

    @Override
    public void loadData(int page) {
        mRefreshLayout.autoRefresh();
        RxJavaManager.getInstance().setTimer(1000)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void onRequestNext(Long entity) {
                        mRefreshLayout.finishRefresh();
                    }

                });
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
    }
}
