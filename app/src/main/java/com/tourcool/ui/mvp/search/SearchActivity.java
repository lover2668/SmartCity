package com.tourcool.ui.mvp.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.util.StatusBarUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.screen.Channel;
import com.tourcool.bean.search.SeachEntity;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.retrofit.repository.ApiRepository;
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
import com.tourcool.ui.mvp.service.SecondaryServiceActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.tourcool.ui.social.SocialBaseInfoActivity;
import com.tourcool.ui.social.detail.SocialListDetailActivity;
import com.tourcool.widget.webview.CommonWebViewActivity;
import com.tourcool.widget.webview.WebViewConstant;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.config.RequestConfig.EXCEPTION_NO_NETWORK;
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
import static com.tourcool.core.constant.ScreenConstant.SUB_COLUMN;
import static com.tourcool.core.constant.ScreenConstant.TIP_WAIT_DEV;
import static com.tourcool.core.constant.SocialConstant.EXTRA_SOCIAL_TYPE;
import static com.tourcool.core.constant.SocialConstant.TIP_GO_CERTIFY;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_RICH_TEXT_ENABLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_TITLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_URL;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年11月04日17:56
 * @Email: 971613168@qq.com
 */
public class SearchActivity extends BaseCommonTitleActivity implements View.OnClickListener {
    private EditText etSearch;
    private LinearLayout llContainer;
    private List<View> viewList = new ArrayList<>();
    private ImageView ivClearInput;

    @Override
    public int getContentLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initSearchView();
        findViewById(R.id.ivSearch).setOnClickListener(this);
        etSearch = findViewById(R.id.etSearch);
        llContainer = findViewById(R.id.llContainer);
        ivClearInput = findViewById(R.id.ivClearInput);
        findViewById(R.id.ivBack).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }


    private void initSearchView() {
        RelativeLayout rlSearch = findViewById(R.id.rlSearch);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
        rlSearch.setLayoutParams(params);
    }

    @Override
    public void loadData() {
        listenSearch();
        listenInput(etSearch, ivClearInput);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                doSearch();
                //  这里记得一定要将键盘隐藏
                KeyboardHelper.closeKeyboard(mContext);
                break;
            case R.id.ivBack:
                finish();
                break;
            default:
                break;
        }
    }

    private void requestSearch(String keyword) {
        removeAllView();
        ApiRepository.getInstance().requestSearch(keyword).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult<SeachEntity>>() {
                    @Override
                    public void onRequestNext(BaseResult<SeachEntity> entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            handleCallback(entity.data);
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        if (e.toString().contains(EXCEPTION_NO_NETWORK)) {
                            loadNoNetworkView();
                        }

                    }
                });
    }


    private void doSearch() {
        if (TextUtils.isEmpty(getTextValue(etSearch))) {
            ToastUtil.show("请输入搜索内容");
            return;
        }
        requestSearch(getTextValue(etSearch));
    }

    private void listenSearch() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = etSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(key)) {
                        ToastUtil.show("请输入搜索内容");
                        return true;
                    }
                    //  下面是业务逻辑
                    doSearch();
                    //  这里记得一定要将键盘隐藏
                    KeyboardHelper.closeKeyboard(mContext);
                    return true;
                }
                return false;
            }
        });
    }


    private void handleCallback(SeachEntity entity) {
        if (entity == null) {
            return;
        }
        removeAllView();
        List<Channel> channelList = entity.getChannels();
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        if (channelList != null) {
            for (Channel channel : channelList) {
                MatrixBean matrixBean = transformMatrix(channel);
                if (matrixBean != null) {
                    matrixBeanList.add(matrixBean);
                }
            }
        }
        if (!matrixBeanList.isEmpty()) {
            loadMatrixLayout(matrixBeanList);
        } else {
            loadEmptyView();
        }
    }


    private MatrixBean transformMatrix(Channel channel) {
        if (channel == null) {
            return null;
        }
        TourCooLogUtil.i("---------transformMatrix",channel);
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setLink(TourCooUtil.getNotNullValue(channel.getLink()));
        matrixBean.setMatrixName(channel.getName());
        matrixBean.setMatrixTitle(channel.getTitle());
        matrixBean.setJumpWay(channel.getJumpWay());
        matrixBean.setChildren(channel.getChildren());
        matrixBean.setType(channel.getType());
        matrixBean.setColumnName("服务");
        matrixBean.setParentsName(channel.getName());
        //取方形图标
        if (TextUtils.isEmpty(channel.getCircleIcon())) {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getIcon()));
        } else {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getCircleIcon()));
        }
        matrixBean.setContent(channel.getContent());
        matrixBean.setRichText(channel.getContent());
        return matrixBean;
    }


    private void loadMatrixLayout(List<MatrixBean> matrixBeanList) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_two_level_layout, null);
        TextView tvGroupName = rootView.findViewById(R.id.tvGroupName);
        ImageView ivSkip = rootView.findViewById(R.id.ivSkip);
        setViewGone(ivSkip, false);
        String groupName = "服务";
        tvGroupName.setText(groupName);
        RecyclerView rvCommonChild = rootView.findViewById(R.id.rvCommonChild);
        MatrixAdapter adapter = new MatrixAdapter();
        //二级布局为网格布局
        rvCommonChild.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter.bindToRecyclerView(rvCommonChild);
        adapter.setNewData(matrixBeanList);
        llContainer.addView(rootView);
        viewList.add(rootView);
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        rootView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        setClickListener(adapter);
    }


    /**
     * 分割线
     */
    private View createLineView() {
        return LayoutInflater.from(mContext).inflate(R.layout.line_view_verticle_layout, null);
    }


    private void loadEmptyView() {
        View emptyView = View.inflate(mContext, R.layout.common_status_layout_empty, null);
        removeAllView();
        llContainer.addView(emptyView);
        viewList.add(emptyView);
    }

    private void loadNoNetworkView() {
        View noNetworkView = View.inflate(mContext, R.layout.common_status_layout_no_network, null);
        removeAllView();
        llContainer.addView(noNetworkView);
        viewList.add(noNetworkView);
        LinearLayout llNoNetwok = noNetworkView.findViewById(R.id.llNoNetwok);
        llNoNetwok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }


    private void removeAllView() {
        llContainer.removeAllViews();
        viewList.clear();
    }

    private void listenInput(EditText editText, ImageView imageView) {
        setViewVisible(imageView, !TextUtils.isEmpty(editText.getText().toString()));
        imageView.setOnClickListener(v -> editText.setText(""));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setViewVisible(imageView, s.length() != 0);
            }
        });
    }

    private void setClickListener(MatrixAdapter adapter) {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MatrixBean matrixBean = (MatrixBean) adapter.getData().get(position);
                switch (TourCooUtil.getNotNullValue(matrixBean.getType())) {
                    case SUB_COLUMN:
                        List<Channel> channelList = new ArrayList<>();
                        if (matrixBean.getChildren() != null) {
                            channelList.addAll(parseChannelList(matrixBean.getChildren()));
                        }
                        Intent intent = new Intent();
                        intent.setClass(mContext, SecondaryServiceActivity.class);
                        intent.putExtra("columnName", matrixBean.getColumnName());
                        intent.putExtra("groupName", matrixBean.getParentsName());
                        intent.putExtra("channelList", (Serializable) channelList);
                        TourCooLogUtil.i("channelList=", channelList);
//                intent.putExtra("secondService", item.getChildren());
                        startActivity(intent);
                        break;
                    default:
                       /* if(ITEM_TYPE_KITCHEN.equals(matrixBean.getMatrixName())){
                            skipBrightKitchen();
                        }else if(ITEM_TYPE_PARKING.equals(matrixBean.getMatrixName())){
                            skipParking();
                        }else{
                            WebViewActivity.start(mContext, TourCooUtil.getUrl(matrixBean.getLink()),true);
                        }*/
                    TourCooLogUtil.i("---------",matrixBean);
                        skipByMatrix(matrixBean);
                        break;
                }

            }
        });
    }


    private List<Channel> parseChannelList(Object children) {
        List<Channel> channelList = new ArrayList<>();
        if (children == null) {
            return channelList;
        }
        String jsonData = JSON.toJSONString(children);
        JSONArray jsonArray = JSON.parseArray(jsonData);
        TourCooLogUtil.i(TAG, jsonArray);
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
//            JSONObject detail = (JSONObject) jsonObject.g;
            if (jsonObject == null) {
                continue;
            }
            Channel channel = JSON.parseObject(jsonObject.toJSONString(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        return channelList;
    }

    private void skipBrightKitchen() {
        Intent intent = new Intent();
//        intent.setClass(mContext, DeviceListActivity.class);
        intent.setClass(mContext, VideoListActivity.class);
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
        if (!AccountHelper.getInstance().isLogin()) {
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
        if (!AccountHelper.getInstance().isLogin()) {
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


    /*private void skipByParams(String title, String link) {
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
                WebViewActivity.start(mContext, StringUtil.getNotNullValue(link));
                break;
        }
    }*/
    /*private void skipByCondition(String title, String link) {
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
                skipWebView(link, title);
                break;
        }
    }*/
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

    private void skipWebView(String link, String title) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WEB_VIEW_URL, link);
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, false);
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, title);
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }

    private void skipWebViewRich(String richContent, String urlTitle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, true);
        intent.putExtra(EXTRA_WEB_VIEW_URL, "");
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, urlTitle);
        WebViewConstant.richText = richContent;
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }


    private void skipByMatrix(MatrixBean matrixBean) {
        switch (matrixBean.getJumpWay()) {
            case CLICK_TYPE_LINK_OUTER:
                //展示外链
                skipWebView(StringUtil.getNotNullValue(matrixBean.getLink()), matrixBean.getMatrixTitle());
                break;
            case CLICK_TYPE_NONE:
//                        ToastUtil.show("什么也不做");
                break;
            case CLICK_TYPE_NATIVE:
                //展示原生
                skipNativeByCondition(matrixBean.getMatrixName(), matrixBean.getLink());
                break;
            case CLICK_TYPE_LINK_INNER:
                //展示外链
                skipWebViewRich(matrixBean.getRichText(), matrixBean.getMatrixTitle());
                break;

            case CLICK_TYPE_WAITING:
                //待开发
                ToastUtil.show(TIP_WAIT_DEV);
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
