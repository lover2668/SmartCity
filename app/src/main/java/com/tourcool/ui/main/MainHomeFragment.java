package com.tourcool.ui.main;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aries.ui.util.StatusBarUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.NetworkUtil;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.adapter.TwoLevelChildAdapter;
import com.tourcool.bean.ElemeGroupedItem;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.home.HomeBean;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.bean.home.Weather;
import com.tourcool.bean.screen.Channel;
import com.tourcool.bean.screen.ChildNode;
import com.tourcool.bean.screen.ColumnItem;
import com.tourcool.bean.screen.ScreenEntity;
import com.tourcool.bean.screen.ScreenPart;
import com.tourcool.bean.weather.SimpleWeather;
import com.tourcool.core.MyApplication;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.ItemConstant;
import com.tourcool.core.constant.ScreenConsrant;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.DateUtil;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.tourcool.ui.mvp.contract.MessageContract;
import com.tourcool.ui.mvp.service.SecondaryServiceActivity;
import com.tourcool.ui.mvp.service.ServiceActivity;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

import static com.frame.library.core.util.StringUtil.LINE_HORIZONTAL;
import static com.frame.library.core.util.StringUtil.SYMBOL_TEMP;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_IMAGE;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_SEARCH;
import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_WEATHER;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_NONE;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_URL;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_CONTAINS_SUBLISTS;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_HORIZONTAL_BANNER;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_IMAGE;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_IMAGE_TEXT_LIST;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_VERTICAL_BANNER;
import static com.tourcool.core.constant.ScreenConsrant.SUB_CHANNEL;
import static com.tourcool.core.constant.ScreenConsrant.SUB_COLUMN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_DUO_YUN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_QING;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日21:26
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class MainHomeFragment extends BaseTitleFragment implements OnRefreshListener, View.OnClickListener {
    private static final String AIR_QUALITY = "空气质量";
    public static final String EXTRA_CLASSIFY_NAME = "EXTRA_CLASSIFY_NAME";
    public static final String EXTRA_FIRST_CHILD_ID = "EXTRA_FIRST_CHILD_ID";
    private SmartRefreshLayout mRefreshLayout;
    private LinearLayout llContainer;
    private Handler mHandler = new Handler();
    private List<View> viewList = new ArrayList<>();
    private boolean isDarkFont = false;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_main_home_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        llContainer = mContentView.findViewById(R.id.llContainer);
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        mRefreshLayout.setOnRefreshListener(this);
        initSearchView();
        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.colorPrimary));
        mRefreshLayout.setEnableHeaderTranslationContent(true)
                .setEnableOverScrollDrag(true);
        mRefreshLayout.setRefreshHeader(header);
        getHomeInfo();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }


    public static MainHomeFragment newInstance() {
        Bundle args = new Bundle();
        MainHomeFragment fragment = new MainHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void initSearchView() {
        RelativeLayout rlSearch = mContentView.findViewById(R.id.rlSearch);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
        rlSearch.setLayoutParams(params);
        rlSearch.setOnClickListener(this);
    }

    private void setStatusBar(boolean isDarkFont) {
        ImmersionBar.with(MainHomeFragment.this)
                .statusBarDarkFont(isDarkFont, 0.2f)
                .init();
    }


    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setStatusBar(isDarkFont);
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getHomeInfo();
    }

    private void getHomeInfo() {
        if (!NetworkUtil.isConnected(mContext)) {
            loadNetErrorView();
            refreshFinish();
            return;
        }
        ApiRepository.getInstance().requestHomeInfo(1).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult<Object>>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        handleRequestSuccessCallback(entity);
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        TourCooLogUtil.e(TAG, "onRequestError---->" + e.toString());
                        refreshFinish();
                        loadNodataView();
                    }
                });

    }


    /**
     * 切回主线程
     *
     * @param runnable
     */
    private void runMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    /* */

    /**
     * 加载矩阵
     *
     * @param homeBean
     *//*
    private void loadMatrixList(HomeBean homeBean, boolean translate) {
        if (homeBean == null || !ITEM_TYPE_IMAGE_TEXT_LIST.equalsIgnoreCase(homeBean.getType()) || ((HomeChildBean) homeBean.getData()).getChildList() == null) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(mContext).inflate(R.layout.home_recycler_view, null);
        if (recyclerView == null) {
            return;
        }
        recyclerView.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
        MatrixAdapter adapter = new MatrixAdapter();
        //二级布局为网格布局
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        adapter.bindToRecyclerView(recyclerView);
        HomeChildBean homeChildBean = homeBean.getData();
        adapter.setNewData(homeChildBean.getChildList());
        viewList.add(recyclerView);
        llContainer.addView(recyclerView);
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        recyclerView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        if (translate) {
            recyclerView.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
    }
*/
    private void loadImageView(HomeBean homeBean, boolean translate) {
        if (homeBean == null || homeBean.getData() == null || !ITEM_TYPE_IMAGE.equalsIgnoreCase(homeBean.getType())) {
            return;
        }
        ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.image_view_layout, null);
        HomeChildBean homeChildBean = homeBean.getData();
        GlideManager.loadRoundImg(homeChildBean.getIcon(), imageView, 5, R.mipmap.img_placeholder_car, true);
        llContainer.addView(imageView);
        viewList.add(imageView);
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        int marginValue = SizeUtil.dp2px(10f);
        imageView.setPadding(marginValue, 0, marginValue, 0);
        if (translate) {
            imageView.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
    }

    private void removeView() {
        for (View view : viewList) {
            llContainer.removeView(view);
        }
        viewList.clear();
    }


    /**
     * 分割线
     */
    private View createLineView() {
        return LayoutInflater.from(mContext).inflate(R.layout.line_view_verticle_layout, null);
    }


    private void handleRequestSuccessCallback(BaseResult<Object> result) {
        mRefreshLayout.finishRefresh();
        if (result == null) {
            ToastUtil.showFailed("请求失败");
            return;
        }
        if (result.status != RequestConfig.CODE_REQUEST_SUCCESS) {
            ToastUtil.showFailed(result.errorMsg);
            return;
        }
        ThreadPoolManager.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                ScreenEntity screenEntity = parseJavaBean(result.data, ScreenEntity.class);
                if (screenEntity == null) {
                    return;
                }
                runMainThread(() -> {
                    //先移除view 防止重复加载
                    removeView();
                    loadScreenInfo(screenEntity);
                });
            }
        });
    }


    private void loadScreenInfo(ScreenEntity screenEntity) {
      /*  WEATHER(0, "天气样式", new Integer[]{4}),
                HORIZONTAL_BANNER(1, "水平滚动Banner样式", new Integer[]{2}),
                VERTICAL_BANNER(2, "垂直滚动Banner样式", new Integer[]{3}),
                IMAGE_TEXT_LIST(3, "图（上）文（下）列表样式", new Integer[]{0, 1}),
                IMAGE(4, "图片样式", new Integer[]{1}),
                CONTAINS_SUBLISTS(5, "包含子列表样式", new Integer[]{1}),
                TAB(6, "tab 栏选项", new Integer[]{0, 1});*/
        if (screenEntity == null || screenEntity.getChildren() == null) {
            ToastUtil.showFailed("未获取到屏幕配置信息");
            return;
        }
        List<ScreenPart> screenPartList = screenEntity.getChildren();
        loadWeatherLayout(screenEntity);
        for (ScreenPart screenPart : screenPartList) {
            if (screenPart == null) {
                continue;
            }
            switch (screenPart.getLayoutStyle()) {
                case LAYOUT_STYLE_IMAGE_TEXT_LIST:
                    //加载矩阵
                    loadMatrix(getMatrixList(screenPart), false);
                    break;
                case LAYOUT_STYLE_HORIZONTAL_BANNER:
                    //横向banner图
                    loadHorizontalBanner(screenPart);
                    break;
                case LAYOUT_STYLE_VERTICAL_BANNER:
                    //垂直新闻
                    loadHomeViewFlipperData(screenPart, false);
                    break;
                case LAYOUT_STYLE_CONTAINS_SUBLISTS:
                    loadSecondList(screenPart);
                    break;
                case LAYOUT_STYLE_IMAGE:
                    loadImageView(screenPart);
                    break;
                default:
                    break;
            }
        }
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        View lineView1 = createLineView();
        llContainer.addView(lineView1);
        viewList.add(lineView1);
    }

    private List<MatrixBean> getMatrixList(ScreenPart screenPart) {
        if (screenPart == null || screenPart.getLayoutStyle() != LAYOUT_STYLE_IMAGE_TEXT_LIST || screenPart.getChildren() == null) {
            TourCooLogUtil.e(TAG, "未匹配到矩阵数据!");
            return null;
        }
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        List<ChildNode> childNodeList = screenPart.getChildren();
        for (ChildNode childNode : childNodeList) {
            if (childNode == null || (!childNode.isVisible()) || childNode.getDetail() == null) {
                continue;
            }
            MatrixBean matrixBean;
            switch (childNode.getType()) {
                case SUB_CHANNEL:
                    matrixBean = convertMatrix(parseJavaBean(childNode.getDetail(), Channel.class));
                    if (matrixBean == null) {
                        TourCooLogUtil.e(TAG, "matrixBean==null!");
                        return null;
                    }
                    matrixBeanList.add(matrixBean);
                    break;
                case SUB_COLUMN:
                    matrixBean = convertMatrix(screenPart, childNode, parseJavaBean(childNode.getDetail(), ColumnItem.class));
                    if (matrixBean == null) {
                        TourCooLogUtil.e(TAG, "matrixBean==null!");
                        return null;
                    }
                    matrixBeanList.add(matrixBean);
                    break;
                default:
                    break;
            }
        }
        return matrixBeanList;
    }


    private MatrixBean convertMatrix(ScreenPart screenPart, ChildNode childNode, ColumnItem columnItem) {
        if (screenPart == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixName(StringUtil.getNotNullValue(columnItem.getName()));
        matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getIcon()));
        matrixBean.setLink(StringUtil.getNotNullValue(columnItem.getLink()));
        matrixBean.setJumpWay(columnItem.getJumpWay());
        matrixBean.setType(SUB_COLUMN);
        matrixBean.setColumnName(columnItem.getName());
        matrixBean.setChildren(childNode.getChildren());
        matrixBean.setParentsName(screenPart.getColumnName());
        if (TextUtils.isEmpty(columnItem.getCircleIcon())) {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getIcon()));
        } else {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getCircleIcon()));
        }
        matrixBean.setLink(TourCooUtil.getUrl(columnItem.getLink()));
        matrixBean.setId(columnItem.getId());
        return matrixBean;
    }

    private MatrixBean convertMatrix(Channel channel) {
        if (channel == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixName(StringUtil.getNotNullValue(channel.getTitle()));
        matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getCircleIcon()));
        matrixBean.setLink(StringUtil.getNotNullValue(channel.getLink()));
        matrixBean.setJumpWay(channel.getJumpWay());
        return matrixBean;
    }


    private void loadMatrix(List<MatrixBean> matrixList, boolean translate) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, null);
        RecyclerView recyclerView = linearLayout.findViewById(R.id.rvCommon);
        if (recyclerView == null) {
            return;
        }
        recyclerView.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        params.setMargins(0, SizeUtil.dp2px(10), 0, 0);
        recyclerView.setLayoutParams(params);
        MatrixAdapter adapter = new MatrixAdapter();
        //二级布局为网格布局
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setNewData(matrixList);
        viewList.add(linearLayout);
        llContainer.addView(linearLayout);
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        recyclerView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        if (translate) {
            recyclerView.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
        initMatrixClickListener(adapter);
    }


    private void loadHorizontalBanner(ScreenPart screenPart) {
        if (screenPart == null || screenPart.getLayoutStyle() != LAYOUT_STYLE_HORIZONTAL_BANNER || screenPart.getChildren() == null) {
            TourCooLogUtil.e(TAG, "loadHorizentalBanner()--->数据不匹配");
            return;
        }
        List<ChildNode> nodeList = screenPart.getChildren();
        List<Channel> channelList = new ArrayList<>();
        Channel channel;
        for (ChildNode childNode : nodeList) {
            boolean disabled = (childNode == null || (childNode.getDetail() == null) || (!childNode.isVisible()) || (!SUB_CHANNEL.equalsIgnoreCase(childNode.getType())));
            if (disabled) {
                continue;
            }
            channel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        if (channelList.isEmpty()) {
            TourCooLogUtil.e(TAG, "banner数据源为空");
            return;
        }
//        List<String> imageList = new ArrayList<>();
        /*for (Channel currentChannel : channelList) {
            if (currentChannel != null) {
                imageList.add(TourCooUtil.getUrl(currentChannel.getIcon()));
            }
        }*/
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_banner, null);
        BGABanner bgaBanner = linearLayout.findViewById(R.id.banner);
        bgaBanner.setPadding(SizeUtil.dp2px(10), 0, SizeUtil.dp2px(10), 0);
        bgaBanner.setAdapter((banner, itemView, model, position) -> {
            try {
                Channel selectChannel = (Channel) model;
                GlideManager.loadImg(TourCooUtil.getUrl(selectChannel.getIcon()), (ImageView) itemView);
            } catch (Exception e) {
                TourCooLogUtil.e(TAG, "fillBannerItem:" + e.toString());
            }

        });
        bgaBanner.setData(channelList, null);
        bgaBanner.setDelegate((banner, itemView, model, position) -> {
            Channel selectChannel = null;
            try {
                selectChannel = (Channel) model;
            } catch (ClassCastException e) {
                TourCooLogUtil.e(TAG, "fillBannerItem:" + e.toString());
            }
            if (selectChannel != null) {
                String link = TourCooUtil.getUrl(selectChannel.getLink());
                if (!TextUtils.isEmpty(link)) {
                    WebViewActivity.start(mContext, link, true);
                }
            }
        });
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bgaBanner.getLayoutParams();
        params.height = MyApplication.getImageHeight();
        bgaBanner.setTransitionEffect(TransitionEffect.Default);
        viewList.add(linearLayout);
        llContainer.addView(linearLayout);
    }


    /**
     * 加载滚动新闻
     *
     * @param screenPart
     */
    private void loadHomeViewFlipperData(ScreenPart screenPart, boolean translate) {
        if (screenPart == null || ScreenConsrant.LAYOUT_STYLE_VERTICAL_BANNER != screenPart.getLayoutStyle() || screenPart.getChildren() == null) {
            return;
        }
        List<ChildNode> childNodeList = screenPart.getChildren();
        List<Channel> channelList = new ArrayList<>();
        boolean disable;
        Channel channel = null;
        for (ChildNode childNode : childNodeList) {
            disable = childNode == null || childNode.getDetail() == null || (!SUB_CHANNEL.equalsIgnoreCase(childNode.getType()));
            if (disable) {
                continue;
            }
            channel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        if (channelList.isEmpty()) {
            TourCooLogUtil.e(TAG, "channelList为空");
            return;
        }
        View viewFlipperRoot = LayoutInflater.from(mContext).inflate(R.layout.view_flipper_layout, null);
        ImageView ivBulletin = viewFlipperRoot.findViewById(R.id.ivBulletin);
        View viewLineVertical = viewFlipperRoot.findViewById(R.id.viewLineVertical);
        GlideManager.loadRoundImgByListener(TourCooUtil.getUrl(screenPart.getDetail().getIcon()), ivBulletin, 5, TourCooUtil.getDrawable(R.mipmap.ic_avatar_default), false, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                TourCooLogUtil.e("图片加载完成", "onLoadFailed:" + e.toString());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                TourCooLogUtil.i("图片加载完成", "图片高度:" + resource.getIntrinsicHeight() + "图片宽度:" + resource.getIntrinsicWidth());
                //图片宽高比
                float aspectRatio = (float) resource.getIntrinsicHeight() /  (float)resource.getIntrinsicWidth();
                TourCooLogUtil.d(TAG,"图片信息：高宽比-->"+aspectRatio);
                //宽度固定
                float finalWidth = SizeUtil.dp2px(58);
                //最终高度
                float finalHeight = finalWidth * aspectRatio;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivBulletin.getLayoutParams();
                layoutParams.height = (int) finalHeight;
                layoutParams.width = (int) finalWidth;
                viewLineVertical.getLayoutParams().height = (int) (finalHeight + SizeUtil.dp2px(10));
                TourCooLogUtil.d(TAG,"图片信息：宽-->"+layoutParams.width);
                TourCooLogUtil.i(TAG,"图片信息：高-->"+layoutParams.height);
                ivBulletin.setLayoutParams(layoutParams);
                return false;
            }
        });
        View contentLayout;
//        ImageView ivNewsIcon;
        TextView tvNewsContent;
        ViewFlipper homeViewFlipper = viewFlipperRoot.findViewById(R.id.viewFlipper);
        for (Channel newsBean : channelList) {
            if (newsBean == null || TextUtils.isEmpty(newsBean.getTitle())) {
                continue;
            }
            contentLayout = View.inflate(mContext, R.layout.layout_news, null);
//            ivNewsIcon = contentLayout.findViewById(R.id.icBulletin);
            tvNewsContent = contentLayout.findViewById(R.id.tvNewsContent);
            tvNewsContent.setText(newsBean.getTitle());
            Channel finalChannel = channel;
            tvNewsContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (newsBean.getJumpWay()) {
                        case CLICK_TYPE_NATIVE:
                            ToastUtils.showShort("跳转至原生页面");
                            break;
                        case CLICK_TYPE_URL:
                            if (finalChannel != null) {
                                WebViewActivity.start(mContext, finalChannel.getLink());
                            }
                            break;
                        default:
                            ToastUtils.showShort("什么也不做");
                            break;
                    }
                }
            });
            homeViewFlipper.addView(contentLayout);
            //todo
        }
        viewList.add(viewFlipperRoot);
        View lineView = createLineView();
        viewList.add(lineView);
        llContainer.addView(viewFlipperRoot);
        llContainer.addView(lineView);
        if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
    }


    private void loadSecondList(ScreenPart screenPart) {
        boolean illegal = screenPart == null || ScreenConsrant.LAYOUT_STYLE_CONTAINS_SUBLISTS != screenPart.getLayoutStyle() || screenPart.getChildren() == null || screenPart.getDetail() == null;
        if (illegal) {
            return;
        }
        List<ChildNode> nodeList = screenPart.getChildren();
        List<Channel> channelList = new ArrayList<>();
        Channel currentChannel;
        for (ChildNode childNode : nodeList) {
            currentChannel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (currentChannel == null || (!childNode.isVisible())) {
                continue;
            }
            currentChannel.setIcon(TourCooUtil.getUrl(currentChannel.getIcon()));
            channelList.add(currentChannel);
        }
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_two_level_layout, null);
        LinearLayout llServiceHeader = rootView.findViewById(R.id.llServiceHeader);
        TextView tvGroupName = rootView.findViewById(R.id.tvGroupName);
        String groupName = screenPart.getDetail().getName();
        tvGroupName.setText(groupName);
        RecyclerView rvCommonChild = rootView.findViewById(R.id.rvCommonChild);
        TwoLevelChildAdapter adapter = new TwoLevelChildAdapter();
        //二级布局为网格布局
        rvCommonChild.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter.bindToRecyclerView(rvCommonChild);
        adapter.setNewData(channelList);
        llContainer.addView(rootView);
        viewList.add(rootView);
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        rootView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        setSecondLevelListClickListener(adapter);
        llServiceHeader.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(mContext, ServiceActivity.class);
            //把分组名称传过去
            intent.putExtra(EXTRA_CLASSIFY_NAME, groupName);
            if (!adapter.getData().isEmpty()) {
                //把第一个子条目的id传过去
                intent.putExtra(EXTRA_FIRST_CHILD_ID, adapter.getData().get(0).getId());
            }
            startActivity(intent);
        });
        /*if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }*/
    }


    private void loadWeatherLayout(ScreenEntity screenEntity) {
        SimpleWeather weather;
        if (screenEntity == null || screenEntity.getWeather() == null) {
            weather = new SimpleWeather();
            weather.setDate(LINE_HORIZONTAL);
            weather.setTemp(LINE_HORIZONTAL);
            weather.setDate(LINE_HORIZONTAL);
            weather.setQuality(LINE_HORIZONTAL);
        } else {
            weather = screenEntity.getWeather();
        }
        LinearLayout rootView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.home_weather_layout, null);
        RelativeLayout rlWeather = rootView.findViewById(R.id.rlWeather);
        rlWeather.setOnClickListener(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlWeather.getLayoutParams();
        layoutParams.setMargins(SizeUtil.dp2px(10), SizeUtil.dp2px(15), 0, SizeUtil.dp2px(5));
        rlWeather.setLayoutParams(layoutParams);
        TextView tvTemperature = rootView.findViewById(R.id.tvTemperature);
        TextView tvWeatherDesc = rootView.findViewById(R.id.tvWeatherDesc);
        TextView tvAirQuality = rootView.findViewById(R.id.tvAirQuality);
        TextView tvDate = rootView.findViewById(R.id.tvDate);
        ImageView ivWeather = rootView.findViewById(R.id.ivWeather);
        //天气描述
        tvWeatherDesc.setText(weather.getWeather());
        switch (weather.getWeather()) {
            case WEATHER_DUO_YUN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_duoyun, ivWeather);
                break;
            case WEATHER_QING:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_day_qing, ivWeather);
                break;
            default:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_unknown, ivWeather);
                break;
        }
        tvAirQuality.setText(transfirmAirQuailty(weather.getQuality()));
        tvTemperature.setText(transfirmTemp(weather.getTemp()));
        tvDate.setText(transfirmDate(weather.getDate()));
        llContainer.addView(rootView);
        viewList.add(rootView);
    }

    private void loadImageView(ScreenPart screenPart) {
        if (screenPart == null || screenPart.getChildren() == null || LAYOUT_STYLE_IMAGE != screenPart.getLayoutStyle()) {
            return;
        }
        String imageUrl = "";
        List<ChildNode> childNodeList = screenPart.getChildren();
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_image_layout, null);
        ImageView imageView = linearLayout.findViewById(R.id.imageView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.setMargins(SizeUtil.dp2px(15), 0, SizeUtil.dp2px(15), 0);
        imageView.setLayoutParams(layoutParams);
        if (!childNodeList.isEmpty() && childNodeList.get(0) != null) {
            ChildNode childNode = childNodeList.get(0);
            Channel channel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (channel != null) {
                imageUrl = channel.getIcon();
                setImageClickListener(channel, imageView);
            }
        }
        TourCooLogUtil.i(TAG, "图片地址:" + imageUrl);
        GlideManager.loadRoundImg(TourCooUtil.getUrl(imageUrl), imageView, 5, R.mipmap.ic_avatar_default, true);
        llContainer.addView(linearLayout);
        viewList.add(linearLayout);
    }

    private void refreshFinish() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }
    }


    private String transfirmDate(String date) {
        return "[" + DateUtil.formatDateToMonthAndDaySlash(date) + "]";
    }

    private String transfirmAirQuailty(String air) {
        if (TextUtils.isEmpty(air)) {
            return AIR_QUALITY + ": " + LINE_HORIZONTAL;
        }
        return air.contains(AIR_QUALITY) ? air : AIR_QUALITY + ": " + air;
    }

    private String transfirmTemp(String temp) {
        if (TextUtils.isEmpty(temp)) {
            return LINE_HORIZONTAL + SYMBOL_TEMP;
        }
        if (temp.lastIndexOf(SYMBOL_TEMP) != -1) {
            return temp;
        }
        return temp + SYMBOL_TEMP;
    }


    private void initMatrixClickListener(MatrixAdapter adapter) {
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MatrixBean matrixBean = (MatrixBean) adapter1.getData().get(position);
            if (matrixBean == null) {
                return;
            }
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
                    TourCooLogUtil.i(TAG, "channelList=" + channelList.size());
//                intent.putExtra("secondService", item.getChildren());
                    startActivity(intent);
                    break;
                default:
                    WebViewActivity.start(mContext, TourCooUtil.getUrl(matrixBean.getLink()), true);
                    break;
            }

        });
    }


    private void setSecondLevelListClickListener(TwoLevelChildAdapter adapter) {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Channel channel = (Channel) adapter.getData().get(position);
                if (channel == null) {
                    return;
                }
                switch (channel.getJumpWay()) {
                    case CLICK_TYPE_URL:
                        WebViewActivity.start(mContext, TourCooUtil.getUrl(channel.getLink()));
                        break;
                    case CLICK_TYPE_NONE:
                        ToastUtil.show("什么也不做");
                        break;
                    case CLICK_TYPE_NATIVE:
                        ToastUtil.show("跳转原生页面");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlWeather:
                ARouter.getInstance().build(ACTIVITY_URL_WEATHER).navigation();
                break;
            case R.id.rlSearch:
                ARouter.getInstance().build(ACTIVITY_URL_SEARCH).navigation();
                break;
            default:
                break;
        }
    }


    private List<Channel> parseChannelList(Object children) {
        List<Channel> channelList = new ArrayList<>();
        String jsonData = JSON.toJSONString(children);
        JSONArray jsonArray = JSON.parseArray(jsonData);
        TourCooLogUtil.i(TAG, jsonArray);
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
            JSONObject detail = (JSONObject) jsonObject.get("detail");
            if (detail == null) {
                continue;
            }
            Channel channel = JSON.parseObject(detail.toJSONString(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        return channelList;
    }


    private void loadNetErrorView() {
        View emptyView = View.inflate(mContext, R.layout.view_no_netwrok_layout, null);
        removeAllView();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeInfo();
            }
        });
        llContainer.addView(emptyView);
        viewList.add(emptyView);
    }


    private void loadNodataView() {
        View emptyView = View.inflate(mContext, R.layout.view_no_data_layout, null);
        removeAllView();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeInfo();
            }
        });
        llContainer.addView(emptyView);
        viewList.add(emptyView);
    }

    private void removeAllView() {
        llContainer.removeAllViews();
        viewList.clear();
    }

    private void setImageClickListener(Channel channel, ImageView imageView) {
        if (channel == null || imageView == null) {
            return;
        }
        String link = channel.getLink();
        if (!TextUtils.isEmpty(link)) {
            imageView.setOnClickListener(v -> WebViewActivity.start(mContext, TourCooUtil.getUrl(link), true));
        }
    }
}