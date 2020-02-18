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

import com.blankj.utilcode.util.NetworkUtils;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.NetworkUtil;
import com.frame.library.core.util.ToastUtil;
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
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.smartcity.R;
import com.tourcool.ui.certify.SelectCertifyActivity;
import com.tourcool.ui.kitchen.VideoListActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.mvp.account.PersonalDataActivity;
import com.tourcool.ui.mvp.account.SystemSettingActivity;
import com.tourcool.ui.parking.CarListActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.tourcool.ui.parking.MineParkingActivity;
import com.trello.rxlifecycle3.android.FragmentEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    public static final String MINE_ITEM_NAME_POINT = "我的积分";
    public static final String MINE_ITEM_NAME_CARD = "我的卡";
    public static final String MINE_ITEM_NAME_SOCIAL_SECURITY = "我的社保";
    public static final String MINE_ITEM_NAME_ACCUMULATION_FUND = "我的公积金";
    public static final String MINE_ITEM_NAME_REAL_NAME_AUTHENTICATION = "实名认证";

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
        initTitleBar(titleBar);
        GlideManager.loadCircleImg(R.mipmap.ic_avatar_default, ivAvatar);
        if (AccountHelper.getInstance().isLogin()) {
            setViewGone(llUnlogin, false);
            setViewGone(rlLogin, true);
        } else {
            setViewGone(llUnlogin, true);
            setViewGone(rlLogin, false);
        }
        titleBar.setOnRightTextClickListener(v -> {
            FrameUtil.startActivity(mContext, SystemSettingActivity.class);
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
        MatrixBean matrixBean = new MatrixBean("", MINE_ITEM_NAME_POINT);
        matrixBean.setMatrixIconResourcesId(R.mipmap.ic_mine_point);
        matrixBeanList.add(matrixBean);
        matrixBeanList.add(new MatrixBean("", "", MineMenuAdapter.TYPE_LINE));
        MatrixBean matrixBean1 = new MatrixBean("", MINE_ITEM_NAME_CARD);
        matrixBean1.setMatrixIconResourcesId(R.mipmap.ic_mine_bank_card);
        matrixBeanList.add(matrixBean1);
        matrixBeanList.add(new MatrixBean("", "", MineMenuAdapter.TYPE_LINE));
        MatrixBean matrixBean2 = new MatrixBean("", MINE_ITEM_NAME_SOCIAL_SECURITY);
        matrixBean2.setMatrixIconResourcesId(R.mipmap.ic_mine_social_security);
        matrixBeanList.add(matrixBean2);
        MatrixBean matrixBean3 = new MatrixBean("", MINE_ITEM_NAME_ACCUMULATION_FUND);
        matrixBean3.setMatrixIconResourcesId(R.mipmap.ic_mine_accumulation_fund);
        matrixBeanList.add(matrixBean3);
        matrixBeanList.add(new MatrixBean("", "", MineMenuAdapter.TYPE_LINE));
        MatrixBean matrixBean4 = new MatrixBean("", MINE_ITEM_NAME_REAL_NAME_AUTHENTICATION);
        matrixBean4.setMatrixIconResourcesId(R.mipmap.ic_mine_real_name_authentication);
        matrixBeanList.add(matrixBean4);
        matrixBeanList.add(new MatrixBean("", "", MineMenuAdapter.TYPE_LINE));
        return matrixBeanList;
    }


    private void initAdapter() {
        RecyclerView rvCommon = mContentView.findViewById(R.id.rvCommon);
        rvCommon.setLayoutManager(new LinearLayoutManager(mContext));
        rvCommon.setNestedScrollingEnabled(false);
        menuAdapter = new MineMenuAdapter(new ArrayList<>());
        menuAdapter.bindToRecyclerView(rvCommon);
        menuAdapter.setNewData(getMatrixList());
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (menuAdapter.getData().get(position).getMatrixName()) {
                case MINE_ITEM_NAME_REAL_NAME_AUTHENTICATION:
                    skipCertify();
                    break;
                default:
                    ToastUtil.show("敬请期待");

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
        if (userInfoEvent == null) {
            return;
        }
        TourCooLogUtil.i(TAG, "刷新用户信息");
        doShowUserInfo(userInfoEvent.userInfo);
    }


    private void doShowUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            if (NetworkUtils.isConnected() && AccountHelper.getInstance().isLogin()) {
                doGetUserInfo();
            } else {
                showUserInfo(AccountHelper.getInstance().getUserInfo());
            }

        } else {
            showUserInfo(userInfo);
        }
    }

    private void showUnLogin() {
        setViewGone(llUnlogin, true);
        setViewGone(rlLogin, false);
        GlideManager.loadCircleImg("", ivAvatar);
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
        if (!AccountHelper.getInstance().isLogin()) {
            showUnLogin();
            return;
        }
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        finishRefresh();
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            UserInfo userInfo = parseJavaBean(entity.data, UserInfo.class);
                            if (userInfo == null) {
                                return;
                            }
                            doShowUserInfo(userInfo);
                            AccountHelper.getInstance().saveUserInfoToDisk(userInfo);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        finishRefresh();
                        TourCooLogUtil.e(TAG, e.toString());
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
        TourCooLogUtil.i(TAG, "用户信息=" + AccountHelper.getInstance().getUserInfo());
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

    private void initTitleBar(TitleBarView titleBar) {
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
        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
    }


    private void skipCertify() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, SelectCertifyActivity.class);

            startActivity(intent);
        }

    }
    private void skipParking() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, FastParkingActivity.class);
            startActivity(intent);
        }

    }

    private void skipLogin() {
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        startActivity(intent);
    }



}
