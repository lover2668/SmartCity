package com.tourcool.ui.mvp.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.screen.Channel;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.tourcool.ui.base.BaseCommonTitleActivity;
import com.tourcool.ui.kitchen.VideoListActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.tourcool.ui.social.SocialBaseInfoActivity;
import com.tourcool.ui.social.detail.SocialListDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_KITCHEN;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_PARKING;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_BASE_INFO;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_BIRTH;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_GS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_NONE;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_URL;
import static com.tourcool.core.constant.SocialConstant.EXTRA_SOCIAL_TYPE;
import static com.tourcool.core.constant.SocialConstant.TIP_GO_CERTIFY;

/**
 * @author :JenkinsZhou
 * @description :服务二级页面
 * @company :途酷科技
 * @date 2019年11月06日10:25
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class SecondaryServiceActivity extends BaseCommonTitleActivity {
    private List<Channel> mChannelList = new ArrayList<>();
    private TextView tvGroupName;

    @Override
    public int getContentLayout() {
        return R.layout.activity_secondary_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvGroupName = findViewById(R.id.tvHeaderTitle);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        String title = getIntent().getStringExtra("columnName");
        String groupName = getIntent().getStringExtra("groupName");
        tvGroupName.setText(groupName);
        titleBar.setTitleMainText(title);
        List<Channel> channelList = (List<Channel>) getIntent().getSerializableExtra("channelList");
        TourCooLogUtil.i(TAG, channelList);
        if (channelList != null) {
            mChannelList.addAll(channelList);
        }
    }

    @Override
    public void loadData() {
        setAdapter();
    }

    private void setAdapter() {
        MatrixAdapter adapter = new MatrixAdapter();
        //二级布局为网格布局
        RecyclerView rvCommon = findViewById(R.id.rvCommon);
        rvCommon.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter.bindToRecyclerView(rvCommon);
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        for (Channel channel : mChannelList) {
            TourCooLogUtil.i(TAG, "channelName=" + channel.getName());
            MatrixBean matrixBean = parseMatrix(channel);
            if (matrixBean != null) {
                matrixBeanList.add(matrixBean);
            }
        }
        View emptyView = View.inflate(mContext, R.layout.common_status_layout_empty, null);
        adapter.setEmptyView(emptyView);
        adapter.setNewData(matrixBeanList);
        initMatrixClickListener(adapter);
    }

    private MatrixBean parseMatrix(Channel channel) {
        if (channel == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getCircleIcon()));
        matrixBean.setLink(channel.getLink());
        matrixBean.setMatrixName(channel.getName());
        matrixBean.setMatrixTitle(channel.getTitle());
        matrixBean.setJumpWay(channel.getJumpWay());
        return matrixBean;
    }


    private void initMatrixClickListener(MatrixAdapter adapter) {
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MatrixBean matrixBean = (MatrixBean) adapter1.getData().get(position);
            if (matrixBean == null) {
                return;
            }
            switch (matrixBean.getJumpWay()) {
                case CLICK_TYPE_URL:
                case CLICK_TYPE_NONE:
                    skipByParams(matrixBean.getMatrixTitle(),matrixBean.getMatrixName(),matrixBean.getLink());

                  /*  if (ITEM_TYPE_KITCHEN.equals(matrixBean.getMatrixTitle()) || ITEM_TYPE_KITCHEN.equals(matrixBean.getMatrixName())) {
                        skipBrightKitchen();
                    } else if (ITEM_TYPE_PARKING.equals(matrixBean.getMatrixTitle()) || ITEM_TYPE_PARKING.equals(matrixBean.getMatrixName())) {
                        skipParking();
                    } else {
                        WebViewActivity.start(mContext, TourCooUtil.getUrl(matrixBean.getLink()));
                    }*/
                    break;
                case CLICK_TYPE_NATIVE:
                    ToastUtil.show("跳转原生页面");
                    break;
                default:
                    break;
            }
        });
    }

    private void skipBrightKitchen() {
        Intent intent = new Intent();
        intent.setClass(mContext, VideoListActivity.class);
//        intent.setClass(mContext, DeviceListActivity.class);
        startActivity(intent);
    }

    private void skipParking() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, FastParkingActivity.class);
        startActivity(intent);
    }

    private void skipLogin() {
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        startActivity(intent);
    }

    private void skipByParams(String title, String secondTitle, String link) {
        switch (StringUtil.getNotNullValue(title)) {
            case ITEM_TYPE_SOCIAL_BASE_INFO:
                skipSocialBase();
                break;
            case ITEM_TYPE_SOCIAL_QUERY_GS:
            case ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER:
            case ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK:
            case ITEM_TYPE_SOCIAL_QUERY_BIRTH:
                skipSocialListDetail(title);
                break;
            case ITEM_TYPE_KITCHEN:
                skipBrightKitchen();
                break;
            case ITEM_TYPE_PARKING:
                skipParking();
                break;
            default:
//                WebViewActivity.start(mContext, StringUtil.getNotNullValue(link));
                switch (StringUtil.getNotNullValue(secondTitle)) {
                    case ITEM_TYPE_SOCIAL_BASE_INFO:
                        skipSocialBase();
                        break;
                    case ITEM_TYPE_SOCIAL_QUERY_GS:
                    case ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER:
                    case ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK:
                    case ITEM_TYPE_SOCIAL_QUERY_BIRTH:
                        skipSocialListDetail(title);
                        break;
                    case ITEM_TYPE_KITCHEN:
                        skipBrightKitchen();
                        break;
                    case ITEM_TYPE_PARKING:
                        skipParking();
                        break;
                    default:
                        WebViewActivity.start(mContext, TourCooUtil.getUrl(link));
                        break;
                }

                break;
        }
    }

    private void skipSocialBase() {
        if(!AccountHelper.getInstance().isLogin()){
            skipLogin();
            return;
        }
        if (!AccountHelper.getInstance().getUserInfo().isVerified()) {
            ToastUtil.show(TIP_GO_CERTIFY);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, SocialBaseInfoActivity.class);
        startActivity(intent);
    }

    private void skipSocialListDetail(String type) {
        if(!AccountHelper.getInstance().isLogin()){
            skipLogin();
            return;
        }
        if (!AccountHelper.getInstance().getUserInfo().isVerified()) {
            ToastUtil.show(TIP_GO_CERTIFY);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, SocialListDetailActivity.class);
        intent.putExtra(EXTRA_SOCIAL_TYPE, type);
        startActivity(intent);
    }
}
