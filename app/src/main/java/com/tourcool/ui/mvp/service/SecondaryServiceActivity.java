package com.tourcool.ui.mvp.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.screen.Channel;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.tourcool.ui.base.BaseCommonTitleActivity;
import com.tourcool.ui.calender.YellowCalenderDetailActivity;
import com.tourcool.ui.certify.SelectCertifyActivity;
import com.tourcool.ui.constellation.ConstellationListActivity;
import com.tourcool.ui.driver.AgainstScoreQueryActivity;
import com.tourcool.ui.driver.DriverIllegalQueryActivity;
import com.tourcool.ui.express.ExpressQueryActivity;
import com.tourcool.ui.garbage.GarbageQueryActivity;
import com.tourcool.ui.kitchen.VideoListActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.tourcool.ui.social.SocialBaseInfoActivity;
import com.tourcool.ui.social.detail.SocialListDetailActivity;
import com.tourcool.widget.webview.CommonWebViewActivity;
import com.tourcool.widget.webview.WebViewConstant;

import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CERTIFY_NAME;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CONSTELLATION;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_DRIVER_AGAINST;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_DRIVER_SCORE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_EXPRESS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_GARBAGE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_KITCHEN;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_PARKING;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_BASE_INFO;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_BIRTH;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_GS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_YELLOW_CALENDER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_LINK_INNER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_LINK_OUTER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_NONE;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_WAITING;
import static com.tourcool.core.constant.ScreenConstant.TIP_WAIT_DEV;
import static com.tourcool.core.constant.SocialConstant.EXTRA_SOCIAL_TYPE;
import static com.tourcool.core.constant.SocialConstant.TIP_GO_CERTIFY;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_RICH_TEXT_ENABLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_TITLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_URL;

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
            LogUtils.i(TAG, "channelName=" + channel.getName());
            LogUtils.i(TAG, "channel=" + channel.getContent());
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
        matrixBean.setRichText(channel.getContent());
        matrixBean.setMatrixName(channel.getName());
        matrixBean.setMatrixTitle(channel.getTitle());
        matrixBean.setContent(channel.getContent());
        matrixBean.setJumpWay(channel.getJumpWay());
        LogUtils.i("富文本内容："+channel.getContent());
        matrixBean.setContent(channel.getContent());
        return matrixBean;
    }


    private void initMatrixClickListener(MatrixAdapter adapter) {
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MatrixBean matrixBean = (MatrixBean) adapter1.getData().get(position);
            if (matrixBean == null) {
                return;
            }
         skipByMatrixBean(matrixBean);
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


    private void skipByMatrixBean(MatrixBean matrixBean){
        switch (matrixBean.getJumpWay()) {
            case CLICK_TYPE_LINK_OUTER:
                //展示外链
                skipWebView(StringUtil.getNotNullValue(matrixBean.getLink()),matrixBean.getMatrixTitle());
                break;
            case CLICK_TYPE_NONE:
//                        ToastUtil.show("什么也不做");
                break;
            case CLICK_TYPE_NATIVE:
                //展示原生
                skipNativeByCondition(matrixBean.getMatrixTitle(), matrixBean.getLink());
                break;
            case CLICK_TYPE_LINK_INNER:
                //展示外链
                skipWebViewRich(matrixBean.getContent(),matrixBean.getMatrixTitle());
                break;

            case CLICK_TYPE_WAITING:
                //待开发
                ToastUtil.show(TIP_WAIT_DEV);
                break;
            default:
                break;
        }
    }

    private void skipWebView(String link,String title) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WEB_VIEW_URL, link);
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, false);
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, title);
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }

    private void skipWebViewRich(String richContent,String title) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, true);
        intent.putExtra(EXTRA_WEB_VIEW_URL, "");
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, title);
        WebViewConstant.richText = richContent;
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }

   /* private void skipByCondition(String title, String link) {
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
            case ITEM_TYPE_CONSTELLATION:
                skipConstellation();
                break;
            case ITEM_TYPE_EXPRESS:
                skipExpress();
                break;
            case ITEM_TYPE_GARBAGE:
                skipGarbage();
                break;
            case ITEM_TYPE_YELLOW_CALENDER:
                skipYellowCalender();
                break;
            default:
                skipWebView(link,title);
                break;
        }
    }*/

    /**
     * 星座
     */
    private void skipConstellation() {
        Intent intent = new Intent();
        intent.setClass(mContext, ConstellationListActivity.class);
        startActivity(intent);
    }

    /**
     * 快递物流
     */
    private void skipExpress() {
        Intent intent = new Intent();
        intent.setClass(mContext, ExpressQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 快递物流
     */
    private void skipGarbage() {
        Intent intent = new Intent();
        intent.setClass(mContext, GarbageQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 查黄历
     */
    private void skipYellowCalender() {
        Intent intent = new Intent();
        intent.setClass(mContext, YellowCalenderDetailActivity.class);
        startActivity(intent);
    }

    private void skipNativeByCondition(String title, String link) {
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
            case ITEM_TYPE_CONSTELLATION:
                skipConstellation();
                break;
            case ITEM_TYPE_EXPRESS:
                skipExpress();
                break;
            case ITEM_TYPE_GARBAGE:
                skipGarbage();
                break;
            case ITEM_TYPE_YELLOW_CALENDER:
                skipYellowCalender();
                break;
            case ITEM_TYPE_CERTIFY_NAME:
                skipCertify();
                break;
            case ITEM_TYPE_DRIVER_AGAINST:
                skipDriverAgainst();
                break;
            case ITEM_TYPE_DRIVER_SCORE:
                skipDriverScore();
                break;
            default:
                break;
        }
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

    private void skipDriverAgainst() {
        Intent intent = new Intent();
        intent.setClass(mContext, DriverIllegalQueryActivity.class);
        startActivity(intent);
    }

    private void skipDriverScore() {
        Intent intent = new Intent();
        intent.setClass(mContext, AgainstScoreQueryActivity.class);
        startActivity(intent);
    }
}
