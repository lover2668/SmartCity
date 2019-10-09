package com.tourcool.ui.main;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MineMenuAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.core.util.ProgressDrawable;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.helper.EmiRecycleViewDivider;
import com.tourcool.smartcity.R;
import com.tourcool.ui.mvp.account.CertificationRealNameActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.mvp.account.PersonalDataActivity;
import com.tourcool.ui.mvp.account.RegisterActivity;
import com.tourcool.ui.mvp.account.SystemSettingActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月23日11:34
 * @Email: 971613168@qq.com
 */
public class MainMineFragment extends BaseTitleFragment implements OnRefreshListener, View.OnClickListener {
    private SmartRefreshLayout mRefreshLayout;
    private MineMenuAdapter menuAdapter;
    private ImageView ivAvatar;
    private RelativeLayout rlLogin;
    private LinearLayout llUnlogin;

    @Override
    public int getContentLayout() {
        return R.layout.main_fragment_mine;

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.blue79C6FA));
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        ivAvatar = mContentView.findViewById(R.id.ivAvatar);
        rlLogin = mContentView.findViewById(R.id.rlLogin);
        llUnlogin = mContentView.findViewById(R.id.llUnlogin);
        mRefreshLayout.setRefreshHeader(header);
        initAdapter();
        mContentView.findViewById(R.id.tvLoginNow).setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        mRefreshLayout.setOnRefreshListener(this);
        setMarginTop(titleBar);
        TextView mainText = titleBar.getMainTitleTextView();
        titleBar.setTitleMainText("智慧宜兴");
        mainText.setText("");
        titleBar.setRightText("设置");
        titleBar.setRightText("");
        titleBar.setRightTextDrawable(TourCooUtil.getDrawable(R.mipmap.ic_setting));
        mainText.setTextColor(TourCooUtil.getColor(R.color.white));
        GlideManager.loadCircleImg(R.mipmap.img_placeholder_car,ivAvatar);
        setViewGone(llUnlogin, false);
        setViewGone(rlLogin, true);
        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FrameUtil.startActivity(mContext,MyAppManageActivity.class);
                FrameUtil.startActivity(mContext, LoginActivity.class);
//                FrameUtil.startActivity(mContext, SystemSettingActivity.class);
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
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    Intent intent = new Intent();
                    intent.setClass(mContext, CertificationRealNameActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        new Handler().postDelayed(refreshLayout::finishRefresh, 300);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAvatar:
                FrameUtil.startActivity(mContext, PersonalDataActivity.class);
                break;
            case R.id.tvLoginNow:
                FrameUtil.startActivity(mContext, LoginActivity.class);
                break;
            default:
                break;
        }
    }
}