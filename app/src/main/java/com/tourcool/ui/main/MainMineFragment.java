package com.tourcool.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.NetworkUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MineMenuAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.RouteConstance;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.ProgressDrawable;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.helper.EmiRecycleViewDivider;
import com.tourcool.smartcity.R;
import com.tourcool.ui.certify.SelectCertifyActivity;
import com.tourcool.ui.mvp.account.CertificationRealNameActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.mvp.account.PersonalDataActivity;
import com.tourcool.ui.mvp.account.RegisterActivity;
import com.tourcool.ui.mvp.account.SystemSettingActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;

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
    private TextView tvNickName;
    private TextView tvPhone;

    @Override
    public int getContentLayout() {
        return R.layout.main_fragment_mine;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(MainMineFragment.this)) {
            EventBus.getDefault().register(MainMineFragment.this);
        }
        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.blue79C6FA));
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        ivAvatar = mContentView.findViewById(R.id.ivAvatar);
        rlLogin = mContentView.findViewById(R.id.rlLogin);
        llUnlogin = mContentView.findViewById(R.id.llUnlogin);
        tvNickName = mContentView.findViewById(R.id.tvNickName);
        tvPhone = mContentView.findViewById(R.id.tvPhone);
        mRefreshLayout.setRefreshHeader(header);
        initAdapter();
        rlLogin.setOnClickListener(this);
        mContentView.findViewById(R.id.tvLoginNow).setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvNickName.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        showUserInfoFromCache();
        doShowUserInfo();
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
        GlideManager.loadCircleImg(R.mipmap.ic_avatar_default, ivAvatar);
        if (AccountHelper.getInstance().isLogin()) {
            setViewGone(llUnlogin, false);
            setViewGone(rlLogin, true);
        } else {
            setViewGone(llUnlogin, true);
            setViewGone(rlLogin, false);
        }

        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
        titleBar.setOnRightTextClickListener(v -> {
//                FrameUtil.startActivity(mContext,MyAppManageActivity.class);
//            FrameUtil.startActivity(mContext, LoginActivity.class);
//                FrameUtil.startActivity(mContext, SystemSettingActivity.class);
            ARouter.getInstance().build(RouteConstance.ACTIVITY_URL_SETTING).navigation();
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
                    intent.setClass(mContext, SelectCertifyActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    ARouter.getInstance().build(RouteConstance.ACTIVITY_URL_GABAGE_CLASSIFY).navigation();
                    break;
                default:
                    break;
            }
        });
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        doGetUserInfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAvatar:
            case R.id.rlLogin:
                if (AccountHelper.getInstance().isLogin()) {
                    FrameUtil.startActivity(mContext, PersonalDataActivity.class);
                } else {
                    FrameUtil.startActivity(mContext, LoginActivity.class);
                }
                break;
            case R.id.tvLoginNow:
                FrameUtil.startActivity(mContext, LoginActivity.class);
                break;
            default:
                break;
        }
    }


    /**
     * 收到消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoRefreshEvent(UserInfoEvent userInfoEvent) {
        if (userInfoEvent == null || userInfoEvent.userInfo == null) {
            doGetUserInfo();
            return;
        }
        TourCooLogUtil.i(TAG, "刷新用户信息");
        doShowUserInfo(userInfoEvent.userInfo);
    }


    private void doShowUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            showUnLogin();
        } else {
            showUserInfo(userInfo);
        }
    }

    private void showUnLogin() {
        setViewGone(llUnlogin, true);
        setViewGone(rlLogin, false);
        GlideManager.loadCircleImg("",ivAvatar);
    }

    private void showUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            showUnLogin();
            return;
        }
        setViewGone(llUnlogin, false);
        setViewGone(rlLogin, true);
        GlideManager.loadCircleImg(TourCooUtil.getUrl(userInfo.getIconUrl()), ivAvatar);
        tvNickName.setText(TourCooUtil.getNotNullValueLine(userInfo.getNickname()));
        tvPhone.setText(TourCooUtil.getNotNullValueLine(userInfo.getPhoneNumber()));
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(MainMineFragment.this);
        super.onDestroy();
    }


    private void doGetUserInfo() {
        if (!NetworkUtil.isConnected(mContext)) {
            finishRefreshFaild();
            showUserInfoFromCache();
            return;
        }
       /* if(!AccountHelper.getInstance().isLogin()){
            finishRefresh();
            showUserInfoFromCache();
            return;
        }*/
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        finishRefresh();
                        if (entity == null) {
                            showUnLogin();
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            UserInfo userInfo = parseJavaBean(entity.data, UserInfo.class);
                            if (userInfo == null) {
                                showUnLogin();
                                return;
                            }
                            doShowUserInfo(userInfo);
                            AccountHelper.getInstance().saveUserInfoToDisk(userInfo);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        finishRefresh();
                          TourCooLogUtil.e(TAG,e.toString());
                      /*  if(e.toString().contains(RequestConfig.CODE_REQUEST_TOKEN_INVALID+"")){
                            AccountHelper.getInstance().logout();
                            showUserInfo(null);
                        }else {
                            showUserInfoFromCache();
                        }*/
                        showUserInfoFromCache();
                    }
                });
    }

    private void showUserInfoFromCache() {
          TourCooLogUtil.i(TAG,"用户信息="+AccountHelper.getInstance().getUserInfo());
        showUserInfo(AccountHelper.getInstance().getUserInfo());
    }


    private void doShowUserInfo() {
        if (NetworkUtil.isConnected(mContext)) {
            doGetUserInfo();
        } else {
            showUserInfoFromCache();
        }
    }

    private void finishRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }
    }

    private void finishRefreshFaild() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(false);
        }
    }
}
