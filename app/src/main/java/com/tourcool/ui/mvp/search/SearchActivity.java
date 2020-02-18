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
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.screen.Channel;
import com.tourcool.bean.search.SeachEntity;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.tourcool.ui.base.BaseCommonTitleActivity;
import com.tourcool.ui.kitchen.VideoListActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.mvp.service.SecondaryServiceActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.config.RequestConfig.EXCEPTION_NO_NETWORK;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_KITCHEN;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_PARKING;
import static com.tourcool.core.constant.ScreenConsrant.SUB_COLUMN;

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
        if(TextUtils.isEmpty(channel.getCircleIcon())){
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getIcon()));
        }else {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getCircleIcon()));
        }

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
        View emptyView = View.inflate(mContext, R.layout.view_no_data_layout, null);
        removeAllView();
        llContainer.addView(emptyView);
        viewList.add(emptyView);
    }

    private void loadNoNetworkView() {
        View noNetworkView = View.inflate(mContext, R.layout.view_no_netwrok_layout, null);
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
//                        WebViewActivity.start(mContext, TourCooUtil.getUrl(matrixBean.getLink()), true);
                        if(ITEM_TYPE_KITCHEN.equals(matrixBean.getMatrixName())){
                            skipBrightKitchen();
                        }else if(ITEM_TYPE_PARKING.equals(matrixBean.getMatrixName())){
                            skipParking();
                        }else{
                            WebViewActivity.start(mContext, TourCooUtil.getUrl(matrixBean.getLink()),true);
                        }
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
        if(!AccountHelper.getInstance().isLogin()){
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

}
