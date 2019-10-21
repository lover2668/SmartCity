package com.tourcool.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.util.StatusBarUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.FrameUtil;
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
import com.tourcool.core.MyApplication;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.ItemConstant;
import com.tourcool.core.constant.ScreenConsrant;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CONTAINS_SUBLISTS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_HORIZONTAL_BANNER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_IMAGE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_IMAGE_TEXT_LIST;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_VERTICAL_BANNER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_WEATHER;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ScreenConsrant.CLICK_TYPE_URL;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_CONTAINS_SUBLISTS;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_HORIZONTAL_BANNER;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_IMAGE_TEXT_LIST;
import static com.tourcool.core.constant.ScreenConsrant.LAYOUT_STYLE_VERTICAL_BANNER;
import static com.tourcool.core.constant.ScreenConsrant.SUB_CHANNEL;
import static com.tourcool.core.constant.ScreenConsrant.SUB_COLUMN;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日21:26
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class MainHomeFragment extends BaseTitleFragment implements OnRefreshListener {
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
                        ToastUtil.showFailed("请求失败");
                    }
                });
        /*ApiRepository.getInstance().requestHomeInfo(0).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        mRefreshLayout.finishRefresh();
                        if(entity == null||entity.errorMsg == null){
                            ToastUtil.showFailed("请求数据异常");
                            return;
                        }
                        if(entity.status != CODE_REQUEST_SUCCESS){
                            ToastUtil.showFailed("数据解析异常");
                            return;
                        }
                            if (entity.data != null) {
                                ToastUtil.showSuccess("请求成功");
                                handleHomeResultCallback(entity.data);
                            } else {
                                ToastUtil.showFailed(entity.errorMsg);
                            }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        ToastUtil.showFailed("请求失败");
                        mRefreshLayout.finishRefresh(false);
                    }
                });*/
    }


    private void handleHomeView(List<HomeBean> homeList) {
        if (homeList == null) {
            return;
        }
        //先移除view 防止重复加载
        removeView();
        boolean needTranslate;
        HomeBean homeBean;
        for (int i = 0; i < homeList.size(); i++) {
            homeBean = homeList.get(i);
            if (homeBean == null) {
                continue;
            }
            if (i <= 3) {
                needTranslate = true;
            } else {
                needTranslate = false;
            }
            //  根据home实体类型加载数据
            loadUiByHomeBean(homeBean, needTranslate);
        }

    }


    private void loadUiByHomeBean(HomeBean homeBean, boolean translate) {
        if (homeBean == null) {
            return;
        }
        switch (homeBean.getType()) {
            case ITEM_TYPE_WEATHER:
                TourCooLogUtil.d("加载天气布局");
                //天气布局
                loadWeatherLayout(homeBean, translate);
                break;
            case ITEM_TYPE_HORIZONTAL_BANNER:
                TourCooLogUtil.d("ITEM_TYPE_HORIZONTAL_BANNER");
                break;
            case ITEM_TYPE_VERTICAL_BANNER:
                loadHomeViewFlipperData(homeBean, translate);
                break;
            case ITEM_TYPE_CONTAINS_SUBLISTS:
                //二级列表
                loadSecondList(homeBean, translate);
                break;
            case ITEM_TYPE_IMAGE_TEXT_LIST:
                //矩阵样式
//                loadMatrix(homeBean, translate);
                break;
            case ITEM_TYPE_IMAGE:
                loadImageView(homeBean, translate);
                break;
            default:
                break;
        }
    }


    /**
     * 加载滚动新闻
     *
     * @param homeBean
     */
    private void loadHomeViewFlipperData(HomeBean homeBean, boolean translate) {
        if (homeBean == null || !ItemConstant.ITEM_TYPE_VERTICAL_BANNER.equalsIgnoreCase(homeBean.getType()) || homeBean.getData() == null) {
            return;
        }
        View viewFlipperRoot = LayoutInflater.from(mContext).inflate(R.layout.view_flipper_layout, null);
        ImageView ivBulletin = viewFlipperRoot.findViewById(R.id.ivBulletin);
        HomeChildBean homeChildBean = (HomeChildBean) homeBean.getData();
        GlideManager.loadImg(homeChildBean.getIcon(), ivBulletin, R.mipmap.img_placeholder_car);
        View contentLayout;
//        ImageView ivNewsIcon;
        TextView tvNewsContent;
        ViewFlipper homeViewFlipper = viewFlipperRoot.findViewById(R.id.viewFlipper);
        for (HomeChildItem newsBean : homeChildBean.getChildList()) {
            if (newsBean == null || TextUtils.isEmpty(newsBean.getTitle())) {
                continue;
            }
            contentLayout = View.inflate(mContext, R.layout.layout_news, null);
//            ivNewsIcon = contentLayout.findViewById(R.id.icBulletin);
            tvNewsContent = contentLayout.findViewById(R.id.tvNewsContent);
            tvNewsContent.setText(newsBean.getTitle());
            tvNewsContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   switch (newsBean.getClickType()) {
                        case CLICK_TYPE_NATIVE:
                            ToastUtil.showSuccess("跳转原生页面");
                            break;
                        case CLICK_TYPE_URL:
                            ToastUtil.showSuccess("跳转H5页面");
                            break;
                        default:
                            ToastUtil.showSuccess("什么也不做");
                            break;*/
//                    }
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

    /**
     * 加载二级列表
     *
     * @param homeBean
     */
    private void loadSecondList(HomeBean homeBean, boolean translate) {
        boolean illegal = homeBean == null || !ItemConstant.ITEM_TYPE_CONTAINS_SUBLISTS.equalsIgnoreCase(homeBean.getType()) || homeBean.getData() == null || ((HomeChildBean) homeBean.getData()).getChildList() == null || ((HomeChildBean) homeBean.getData()).getChildList().isEmpty();
        if (illegal) {
            return;
        }
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_two_level_layout, null);
        TextView tvGroupName = rootView.findViewById(R.id.tvGroupName);
        HomeChildBean homeChildBean = (HomeChildBean) homeBean.getData();
        tvGroupName.setText(homeChildBean.getTitle());
        RecyclerView rvCommonChild = rootView.findViewById(R.id.rvCommonChild);
        TwoLevelChildAdapter adapter = new TwoLevelChildAdapter();
        //二级布局为网格布局
        rvCommonChild.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter.bindToRecyclerView(rvCommonChild);
//        adapter.setNewData(homeChildBean.getChildList());
        llContainer.addView(rootView);
        viewList.add(rootView);
        View lineView = createLineView();
        llContainer.addView(lineView);
        viewList.add(lineView);
        rootView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
    }


    private void handleHomeResultCallback(Object data) {
        ThreadPoolManager.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                //调试发现 该方法解析javaBean比较耗时 导致主线程短暂卡顿 因此放到子线程解析
                List<HomeBean> homeList = parseJsonToBeanList(data, HomeBean.class);
                if (homeList != null) {
                    runMainThread(() -> {
                        HomeBean homeBean = new HomeBean();
                        homeBean.setType(ITEM_TYPE_IMAGE);
                        HomeChildBean homeChildBean = new HomeChildBean();
                        homeChildBean.setIcon("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1999668027,394700362&fm=11&gp=0.jpg");
                        homeBean.setData(homeChildBean);
                        homeList.add(homeBean);
                        handleHomeView(homeList);
                        TourCooLogUtil.d("数据长度:" + homeList.size());
                    });
                }
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


    private void loadWeatherLayout(HomeBean homeBean, boolean translate) {
        if (homeBean == null || !ITEM_TYPE_WEATHER.equalsIgnoreCase(homeBean.getType()) || homeBean.getWeather() == null) {
            return;
        }
        Weather weather = homeBean.getWeather();
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_layout, null);
        TextView tvTemperature = rootView.findViewById(R.id.tvTemperature);
        TextView tvWeatherDesc = rootView.findViewById(R.id.tvWeatherDesc);
        TextView tvAirQuality = rootView.findViewById(R.id.tvAirQuality);
        TextView tvDate = rootView.findViewById(R.id.tvDate);
        tvWeatherDesc.setText(weather.getWea());
        tvAirQuality.setText(weather.getAir_level());
        tvTemperature.setText(weather.getTem());
        tvDate.setText("[" + weather.getDate() + "]");
        llContainer.addView(rootView);
        viewList.add(rootView);
        if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
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
                    ToastUtil.showSuccess("解析成功");
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
        loadWeatherLayout();
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
                default:
                    break;
            }
        }
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
                    matrixBean = convertMatrix(parseJavaBean(childNode.getDetail(), ColumnItem.class));
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


    private MatrixBean convertMatrix(ColumnItem columnItem) {
        if (columnItem == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixName(StringUtil.getNotNullValue(columnItem.getName()));
        matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getIcon()));
        matrixBean.setLink(StringUtil.getNotNullValue(columnItem.getLink()));
        matrixBean.setJumpWay(columnItem.getJumpWay());
        return matrixBean;
    }

    private MatrixBean convertMatrix(Channel channel) {
        if (channel == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixName(StringUtil.getNotNullValue(channel.getTitle()));
        matrixBean.setMatrixIconUrl(StringUtil.getNotNullValue(channel.getIcon()));
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
        params.setMargins(0,SizeUtil.dp2px(10),0,0);
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
        List<String> imageList = new ArrayList<>();
        for (Channel currentChannel : channelList) {
            if (currentChannel != null) {
                imageList.add(TourCooUtil.getUrl(currentChannel.getIcon()));
            }
        }
        TourCooLogUtil.e(TAG, "imageList长度:" + imageList.size());
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_banner, null);
        BGABanner bgaBanner = linearLayout.findViewById(R.id.banner);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bgaBanner.getLayoutParams();
        layoutParams.setMargins(SizeUtil.dp2px(10),0,SizeUtil.dp2px(10),0);
        bgaBanner.setLayoutParams(layoutParams);
        bgaBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
                GlideManager.loadImg(model, (ImageView) itemView);
                TourCooLogUtil.i(TAG, "图片链接:" + model.toString());
            }
        });
        bgaBanner.setDelegate((banner, itemView, model, position) -> WebViewActivity.start(mContext, model.toString(), false));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bgaBanner.getLayoutParams();
        params.height = MyApplication.getImageHeight();
        bgaBanner.setData(imageList, null);
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
        Channel channel;
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
        GlideManager.loadImg(TourCooUtil.getUrl(screenPart.getDetail().getIcon()), ivBulletin, R.mipmap.img_placeholder_car);
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
            tvNewsContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (newsBean.getJumpWay()) {
                        case CLICK_TYPE_NATIVE:
                            ToastUtils.showShort("跳转至原生页面");
                            break;
                        case CLICK_TYPE_URL:
                            ToastUtils.showShort("跳转至H5页面");
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
        TextView tvGroupName = rootView.findViewById(R.id.tvGroupName);
        tvGroupName.setText(screenPart.getDetail().getName());
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
        /*if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }*/
    }



    private void loadWeatherLayout() {
        Weather weather =new Weather();
        weather.setTem("28°");
        weather.setAir_level("空气质量:轻度污染");
        weather.setWea("多云转晴");
        weather.setDate("10/21");
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_layout, null);
        TextView tvTemperature = rootView.findViewById(R.id.tvTemperature);
        TextView tvWeatherDesc = rootView.findViewById(R.id.tvWeatherDesc);
        TextView tvAirQuality = rootView.findViewById(R.id.tvAirQuality);
        TextView tvDate = rootView.findViewById(R.id.tvDate);
        tvWeatherDesc.setText(weather.getWea());
        tvAirQuality.setText(weather.getAir_level());
        tvTemperature.setText(weather.getTem());
        tvDate.setText("[" + weather.getDate() + "]");
        llContainer.addView(rootView);
        viewList.add(rootView);
    }
}