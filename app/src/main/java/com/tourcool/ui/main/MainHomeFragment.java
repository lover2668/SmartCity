package com.tourcool.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.util.StatusBarUtil;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.adapter.TwoLevelAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.TwoLevelBean;
import com.tourcool.bean.TwoLevelChildBean;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日21:26
 * @Email: 971613168@qq.com
 */
public class MainHomeFragment extends BaseTitleFragment implements OnRefreshListener {
    private MatrixAdapter matrixAdapter;
    private RecyclerView rvMatrix;
    private SmartRefreshLayout mRefreshLayout;


    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_yi_xing;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rvMatrix = mContentView.findViewById(R.id.rvMatrix);
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        mRefreshLayout.setOnRefreshListener(this);
        initSearchView();

        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.colorPrimary));
        mRefreshLayout.setRefreshHeader(header);
        matrixAdapter = new MatrixAdapter();
        rvMatrix.setLayoutManager(new GridLayoutManager(mContext, 5));
        matrixAdapter.bindToRecyclerView(rvMatrix);
        matrixAdapter.setNewData(getMatrixList());
        loadSecondRecyclerView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }


    private List<MatrixBean> getMatrixList() {
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        MatrixBean matrixBean = new MatrixBean("", "巴士管家");
        matrixBeanList.add(matrixBean);
        MatrixBean matrixBean1 = new MatrixBean("", "话费充值");
        matrixBeanList.add(matrixBean1);
        MatrixBean matrixBean2 = new MatrixBean("", "智慧停车");
        matrixBeanList.add(matrixBean2);
        MatrixBean matrixBean3 = new MatrixBean("", "城市公交");
        matrixBeanList.add(matrixBean3);
        MatrixBean matrixBean4 = new MatrixBean("", "违章查询");
        matrixBeanList.add(matrixBean4);
        return matrixBeanList;
    }

    public static MainHomeFragment newInstance() {
        Bundle args = new Bundle();
        MainHomeFragment fragment = new MainHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void initSearchView() {
        RelativeLayout rlSearch = mContentView.findViewById(R.id.rlSearch);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearch.getLayoutParams();
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
        rlSearch.setLayoutParams(params);
    }


    private void setStatusBar() {
        ImmersionBar.with(MainHomeFragment.this)
                .statusBarDarkFont(false, 0.2f)
                .init();
    }


    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setStatusBar();
        }
    }


    private void loadSecondRecyclerView() {
        TwoLevelAdapter twoLevelAdapter = new TwoLevelAdapter();
        RecyclerView recyclerView = mContentView.findViewById(R.id.rvCommon2);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        twoLevelAdapter.bindToRecyclerView(recyclerView);
        twoLevelAdapter.setNewData(getTwoLevelList());
    }


    private List<TwoLevelBean> getTwoLevelList() {
        List<TwoLevelBean> twoLevelBeanList = new ArrayList<>();
        TwoLevelBean twoLevelBean = new TwoLevelBean();
        twoLevelBean.setGroupName("文旅");
        List<TwoLevelChildBean> twoLevelChildBeanList = new ArrayList<>();
        TwoLevelChildBean childBean = new TwoLevelChildBean();
        childBean.setChildItemDesc("最全面的旅游资源");
        childBean.setChildItemTitle("旅游资源");
        twoLevelChildBeanList.add(childBean);
        TwoLevelChildBean childBean1 = new TwoLevelChildBean();
        childBean1.setChildItemDesc("超棒智能旅游体验");
        childBean1.setChildItemTitle("智能体验");
        twoLevelChildBeanList.add(childBean1);
        TwoLevelChildBean childBean2 = new TwoLevelChildBean();
        childBean2.setChildItemDesc("宜兴特色小镇一览");
        childBean2.setChildItemTitle("特色小镇");
        twoLevelChildBeanList.add(childBean2);
        twoLevelBean.setChildBeans(twoLevelChildBeanList);
        twoLevelBeanList.add(twoLevelBean);
        twoLevelBeanList.add((TwoLevelBean) twoLevelBean.clone());
        return twoLevelBeanList;
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.finishRefresh();
            }
        }, 300);
    }
}
