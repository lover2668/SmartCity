package com.tourcool.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MineMenuAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.helper.EmiRecycleViewDivider;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月23日11:34
 * @Email: 971613168@qq.com
 */
public class MainMineFragment extends BaseTitleFragment implements OnRefreshListener {
    private SmartRefreshLayout mRefreshLayout;
    private MineMenuAdapter menuAdapter;

    @Override
    public int getContentLayout() {
        return R.layout.main_fragment_mine;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.colorPrimary));
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        mRefreshLayout.setRefreshHeader(header);
        initAdapter();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        mRefreshLayout.setOnRefreshListener(this);
        TextView mainText = titleBar.getMainTitleTextView();
        titleBar.setTitleMainText("智慧宜兴");
        mainText.setText("");
        titleBar.setRightText("设置");
        titleBar.setRightText("");
        titleBar.setRightTextDrawable(TourCooUtil.getDrawable(R.mipmap.ic_setting));
        mainText.setTextColor(TourCooUtil.getColor(R.color.white));
        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameUtil.startActivity(mContext,MyAppManageActivity.class);
            }
        });
    }


    public static MainMineFragment newInstance() {
        Bundle args = new Bundle();
        MainMineFragment fragment = new MainMineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private List<MatrixBean> getMatrixList() {
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        MatrixBean matrixBean = new MatrixBean("", "我的积分");
        matrixBeanList.add(matrixBean);
        MatrixBean matrixBean1 = new MatrixBean("", "我的卡");
        matrixBeanList.add(matrixBean1);
        MatrixBean matrixBean2 = new MatrixBean("", "我的社保");
        matrixBeanList.add(matrixBean2);
        MatrixBean matrixBean3 = new MatrixBean("", "我的公积金");
        matrixBeanList.add(matrixBean3);
        MatrixBean matrixBean4 = new MatrixBean("", "违章查询");
        matrixBeanList.add(matrixBean4);
        return matrixBeanList;
    }


    private void initAdapter() {
        RecyclerView rvCommon = mContentView.findViewById(R.id.rvCommon);
        rvCommon.setLayoutManager(new LinearLayoutManager(mContext));
        menuAdapter = new MineMenuAdapter();
        menuAdapter.bindToRecyclerView(rvCommon);
        menuAdapter.setNewData(getMatrixList());
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
