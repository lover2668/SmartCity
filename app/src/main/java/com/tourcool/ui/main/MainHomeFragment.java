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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.util.StatusBarUtil;
import com.blankj.utilcode.util.LogUtils;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.adapter.MatrixOldAdapter;
import com.tourcool.adapter.TwoLevelChildAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.TwoLevelBean;
import com.tourcool.bean.TwoLevelChildBean;
import com.tourcool.bean.home.HomeBean;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.constant.ItemConstant;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.library.frame.R;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.constant.ClickConstant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ClickConstant.CLICK_TYPE_URL;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CONTAINS_SUBLISTS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_HORIZONTAL_BANNER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_IMAGE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_IMAGE_TEXT_LIST;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_VERTICAL_BANNER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_WEATHER;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日21:26
 * @Email: 971613168@qq.com
 */
public class MainHomeFragment extends BaseTitleFragment implements OnRefreshListener {
    private SmartRefreshLayout mRefreshLayout;
    private LinearLayout llContainer;
    private Handler mHandler = new Handler();
    private List<View> viewList = new ArrayList<>();
    private boolean isDarkFont = false;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_yi_xing;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        llContainer = mContentView.findViewById(R.id.llContainer);
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        mRefreshLayout.setOnRefreshListener(this);
        initSearchView();
        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.colorPrimary));
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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearch.getLayoutParams();
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
        ApiRepository.getInstance().requestHomeInfo("宜兴", true).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        mRefreshLayout.finishRefresh();
                        if (entity != null && entity.data != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                ToastUtil.showSuccess("请求成功");
                                handleHomeResultCallback(entity.data);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        ToastUtil.showFailed("请求失败");
                        mRefreshLayout.finishLoadMore();
                    }
                });
    }


    private void handleHomeView(List<HomeBean> homeList) {
        if (homeList == null) {
            return;
        }
        //先移除view 防止重复加载
        removeView();
        for (HomeBean homeBean : homeList) {
            if (homeBean == null) {
                continue;
            }
            TourCooLogUtil.d("UI类型:" + homeBean.getType());
            LogUtils.i("UI类型:" + homeBean.getType());
            //  根据home实体类型加载数据
            loadUiByHomeBean(homeBean);
        }
    }


    private void loadUiByHomeBean(HomeBean homeBean) {
        if (homeBean == null) {
            return;
        }
        switch (homeBean.getType()) {
            case ITEM_TYPE_WEATHER:
                TourCooLogUtil.d("");
                break;
            case ITEM_TYPE_HORIZONTAL_BANNER:
                TourCooLogUtil.d("ITEM_TYPE_HORIZONTAL_BANNER");
                break;
            case ITEM_TYPE_VERTICAL_BANNER:
                TourCooLogUtil.i("执行了");
                loadHomeViewFlipperData(homeBean);
                break;
            case ITEM_TYPE_CONTAINS_SUBLISTS:
                //二级列表
                TourCooLogUtil.i("执行了");
                loadSecondList(homeBean);
                break;
            case ITEM_TYPE_IMAGE_TEXT_LIST:
                //矩阵样式
                TourCooLogUtil.i("执行了");
                loadMatrixList(homeBean);
                break;
            case ITEM_TYPE_IMAGE:
                TourCooLogUtil.i("执行了");
                loadImageView(homeBean);
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
    private void loadHomeViewFlipperData(HomeBean homeBean) {
        if (homeBean == null || !ItemConstant.ITEM_TYPE_VERTICAL_BANNER.equalsIgnoreCase(homeBean.getType()) || homeBean.getData() == null) {
            return;
        }
        View viewFlipperRoot = LayoutInflater.from(mContext).inflate(R.layout.view_flipper_layout, null);
        ImageView ivBulletin = viewFlipperRoot.findViewById(R.id.ivBulletin);
        GlideManager.loadImg(homeBean.getData().getIcon(), ivBulletin, R.mipmap.img_placeholder_car);
        View contentLayout;
//        ImageView ivNewsIcon;
        TextView tvNewsContent;
        ViewFlipper homeViewFlipper = viewFlipperRoot.findViewById(R.id.viewFlipper);
        for (HomeChildItem newsBean : homeBean.getData().getChildList()) {
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
                    switch (newsBean.getClickType()) {
                        case CLICK_TYPE_NATIVE:
                            ToastUtil.showSuccess("跳转原生页面");
                            break;
                        case CLICK_TYPE_URL:
                            ToastUtil.showSuccess("跳转H5页面");
                            break;
                        default:
                            ToastUtil.showSuccess("什么也不做");
                            break;
                    }
                }
            });
            homeViewFlipper.addView(contentLayout);
            //todo
        }
        viewList.add(viewFlipperRoot);
        llContainer.addView(viewFlipperRoot);
    }

    /**
     * 加载二级列表
     *
     * @param homeBean
     */
    private void loadSecondList(HomeBean homeBean) {
        boolean illegal = homeBean == null || !ItemConstant.ITEM_TYPE_CONTAINS_SUBLISTS.equalsIgnoreCase(homeBean.getType()) || homeBean.getData() == null || homeBean.getData().getChildList() == null || homeBean.getData().getChildList().isEmpty();
        if (illegal) {
            return;
        }
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_two_level_layout, null);
        TextView tvGroupName = rootView.findViewById(R.id.tvGroupName);
        tvGroupName.setText(homeBean.getData().getTitle());
        RecyclerView rvCommonChild = rootView.findViewById(R.id.rvCommonChild);
        TwoLevelChildAdapter adapter = new TwoLevelChildAdapter();
        //二级布局为网格布局
        rvCommonChild.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter.bindToRecyclerView(rvCommonChild);
        adapter.setNewData(homeBean.getData().getChildList());
        llContainer.addView(rootView);
        viewList.add(rootView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rootView.getLayoutParams();
        layoutParams.setMargins(0, SizeUtil.dp2px(10f), 0, 0);
    }


    private void handleHomeResultCallback(Object data) {
        ThreadPoolManager.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                //调试发现 该方法解析javaBean比较耗时 导致主线程短暂卡顿 因此放到子线程解析
                List<HomeBean> homeList = parseListJavaBean(data, HomeBean.class);
                if (homeList != null) {
                    runMainThread(new Runnable() {
                        @Override
                        public void run() {
                            HomeBean homeBean = new HomeBean();
                            homeBean.setType(ITEM_TYPE_IMAGE);
                            HomeChildBean homeChildBean = new HomeChildBean();
                            homeChildBean.setIcon("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1999668027,394700362&fm=11&gp=0.jpg");
                            homeBean.setData(homeChildBean);
                            homeList.add(homeBean);
                            handleHomeView(homeList);
                            TourCooLogUtil.d("数据长度:" + homeList.size());
                        }
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

    /**
     * 加载矩阵
     *
     * @param homeBean
     */
    private void loadMatrixList(HomeBean homeBean) {
        if (homeBean == null || !ITEM_TYPE_IMAGE_TEXT_LIST.equalsIgnoreCase(homeBean.getType()) || homeBean.getData().getChildList() == null) {
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
        adapter.setNewData(homeBean.getData().getChildList());
        viewList.add(recyclerView);
        llContainer.addView(recyclerView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, SizeUtil.dp2px(10f), 0, 0);
    }


    private void loadImageView(HomeBean homeBean) {
        if (homeBean == null || homeBean.getData() == null || !ITEM_TYPE_IMAGE.equalsIgnoreCase(homeBean.getType())) {
            return;
        }
        ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.image_view_layout, null);
        GlideManager.loadRoundImg(homeBean.getData().getIcon(), imageView, 5, R.mipmap.img_placeholder_car, true);
        llContainer.addView(imageView);
        viewList.add(imageView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        int marginValue = SizeUtil.dp2px(10f);
        layoutParams.setMargins(marginValue, marginValue, marginValue, marginValue);
    }

    private void removeView() {
        for (View view : viewList) {
            llContainer.removeView(view);
        }
        viewList.clear();
    }
}