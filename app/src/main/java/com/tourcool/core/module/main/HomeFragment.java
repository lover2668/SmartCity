package com.tourcool.core.module.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.entity.WidgetEntity;
import com.tourcool.core.helper.CheckVersionHelper;
import com.tourcool.core.helper.TitleBarViewHelper;
import com.frame.library.core.UiManager;
import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.core.MyApplication;
import com.tourcool.core.adapter.WidgetAdapter;
import com.tourcool.core.constant.GlobalConstant;
import com.tourcool.core.constant.SPConstant;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.module.main.sample.FrameViewActivity;
import com.tourcool.core.module.main.sample.QQTitleActivity;
import com.tourcool.core.module.main.sample.SingleFragmentActivity;
import com.tourcool.core.module.main.sample.SwipeBackActivity;
import com.tourcool.core.module.main.sample.TestStatusActivity;
import com.tourcool.core.module.main.sample.TitleWithEditTextActivity;
import com.tourcool.core.module.main.sample.ToastActivity;
import com.tourcool.core.module.main.sample.ali.ALiPayMainActivity;
import com.tourcool.core.module.main.sample.news.NewsMainActivity;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.fragment.BaseTitleRefreshLoadFragment;
import com.frame.library.core.util.SpUtil;
import com.frame.library.core.util.SizeUtil;
import com.aries.ui.util.StatusBarUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.smartcity.R;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

/**
 * @Author: JenkinsZhou on 2018/8/10 12:22
 * @E-Mail: 971613168@qq.com
 * Function: 主页演示
 * Description:
 */
public class HomeFragment extends BaseTitleRefreshLoadFragment<WidgetEntity> {

    protected BGABanner banner;
    private BaseQuickAdapter mAdapter;
    private List<Class> listActivity = new ArrayList<>();
    private List<Integer> listArraysBanner = Arrays.asList(R.array.arrays_banner_all);

    private List<TransitionEffect> listTransitionEffect = new ArrayList<>();
    private int transitionIndex = GlobalConstant.GLOBAL_BANNER_TRANSITION_POSITION;
    private int chooseIndex = 0;
    private AlertDialog alertDialog;
    private boolean mIsLight;
    private TitleBarViewHelper mTitleBarViewHelper;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    public boolean isRefreshEnable() {
        return false;
    }

    @Override
    public BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WidgetAdapter();
        return mAdapter;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setRightTextDrawable(R.drawable.ic_transition)
                .setDividerVisible(false)
//                .setTitleMainText(getClass().getSimpleName())
//                .setTitleMainTextColor(Color.MAGENTA)
                .setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 102)
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertDialog == null) {
                            alertDialog = new AlertDialog.Builder(mContext)
                                    .setTitle("选择banner切换动画")
                                    .setSingleChoiceItems(R.array.arrays_transition, transitionIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            chooseIndex = which;
                                        }
                                    })
                                    .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SpUtil.put(mContext, SPConstant.SP_KEY_HOME_TRANSITION_INDEX, chooseIndex);
                                            transitionIndex = (int) SpUtil.get(mContext, SPConstant.SP_KEY_HOME_TRANSITION_INDEX, transitionIndex);
                                            if (banner != null) {
                                                banner.setTransitionEffect(listTransitionEffect.get(transitionIndex));
                                            }
                                        }
                                    })
                                    .create();
                            alertDialog.getListView().setVerticalScrollBarEnabled(false);
                            alertDialog.getListView().setHorizontalScrollBarEnabled(false);
                        }
                        alertDialog.show();
                    }
                })
                .setStatusAlpha(0)
                .setTitleMainText(R.string.home)
                .setLeftTextColor(Color.WHITE)
                .setBgColor(Color.WHITE);
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        LoggerManager.d(TAG, "initView");
        new CheckVersionHelper(mContext).checkVersion(false);
        listTransitionEffect.add(TransitionEffect.Default);
        listTransitionEffect.add(TransitionEffect.Alpha);
        listTransitionEffect.add(TransitionEffect.Rotate);
        listTransitionEffect.add(TransitionEffect.Cube);
        listTransitionEffect.add(TransitionEffect.Flip);
        listTransitionEffect.add(TransitionEffect.Accordion);
        listTransitionEffect.add(TransitionEffect.ZoomFade);
        listTransitionEffect.add(TransitionEffect.Fade);
        listTransitionEffect.add(TransitionEffect.ZoomCenter);
        listTransitionEffect.add(TransitionEffect.ZoomStack);
        listTransitionEffect.add(TransitionEffect.Stack);
        listTransitionEffect.add(TransitionEffect.Depth);
        listTransitionEffect.add(TransitionEffect.Zoom);
        transitionIndex = (int) SpUtil.get(mContext, SPConstant.SP_KEY_HOME_TRANSITION_INDEX, transitionIndex);
        chooseIndex = transitionIndex;
        setBanner(0);
        int mMaxHeight = MyApplication.getImageHeight() - StatusBarUtil.getStatusBarHeight() - getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        mTitleBarViewHelper = new TitleBarViewHelper(mContext)
                .setTitleBarView(mTitleBar)
                .setRecyclerView(mRecyclerView)
                .setMinHeight(0)
                .setMaxHeight(mMaxHeight)
                .setOnScrollListener(new TitleBarViewHelper.OnScrollListener() {
                    @Override
                    public void onScrollChange(int alpha, boolean isLightMode) {
                        mIsLight = isLightMode;
                        int colorText = Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255);
                        mTitleBar.setTitleMainTextColor(colorText);
                    }
                });
    }

    @Override
    public void loadData(int page) {
        listActivity.clear();
        listActivity.add(FrameViewActivity.class);
        listActivity.add(SwipeBackActivity.class);
        listActivity.add(QQTitleActivity.class);
        listActivity.add(ALiPayMainActivity.class);
        listActivity.add(NewsMainActivity.class);
        listActivity.add(TestStatusActivity.class);
        listActivity.add(SingleFragmentActivity.class);
        listActivity.add(ToastActivity.class);
        listActivity.add(TitleWithEditTextActivity.class);
        List<WidgetEntity> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.arrays_home_list_title);
        for (int i = 0; i < titles.length; i++) {
            WidgetEntity entity = new WidgetEntity();
            entity.title = titles[i];
            entity.content = getString(R.string.home_list_content);
            entity.activity = listActivity.get(i);
            list.add(entity);
        }
        RxJavaManager.getInstance().getDelayObservable(list, 2, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new BaseObserver<List<WidgetEntity>>() {
                    @Override
                    public void onRequestNext(List<WidgetEntity> entity) {
                        UiManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), entity, null);
                    }
                });
    }

    private void setBanner(int position) {
        List<String> images = Arrays.asList(getResources().getStringArray(listArraysBanner.get(position)));
        if (banner == null) {
            View v = View.inflate(mContext, R.layout.layout_banner, null);
            banner = v.findViewById(R.id.banner);
            banner.setAdapter(new BGABanner.Adapter() {
                @Override
                public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
                    GlideManager.loadImg(model, (ImageView) itemView);
                }
            });
            banner.setDelegate(new BGABanner.Delegate() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                    WebViewActivity.start(mContext, model.toString(), false);
                }
            });
            mAdapter.addHeaderView(v);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
            params.height = MyApplication.getImageHeight();
            LoggerManager.d("banner:" + params.height + ";width:" + SizeUtil.getScreenWidth());
        }
        banner.setData(images, getTips(images));
        banner.setTransitionEffect(listTransitionEffect.get(transitionIndex));
        mRefreshLayout.finishRefresh();
    }

    private List<String> getTips(List<String> images) {
        List<String> listTips = new ArrayList<>();
        int size = images == null ? 0 : images.size();
        for (int i = 0; i < size; i++) {
            listTips.add("点击查看原图");
        }
        return listTips;
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        WidgetEntity entity = adapter.getItem(position);
        if (position == 1) {
            SwipeBackActivity.start(mContext, entity.title);
        } else {
            FrameUtil.startActivity(mContext, entity.activity);
        }
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        if (isVisibleToUser) {
            banner.startAutoPlay();
            if (mIsLight) {
                StatusBarUtil.setStatusBarLightMode(mContext);
            } else {
                StatusBarUtil.setStatusBarDarkMode(mContext);
            }
        } else {
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        onVisibleChanged(isVisible());
    }

    @Override
    public void onStop() {
        super.onStop();
        onVisibleChanged(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTitleBarViewHelper != null) {
            mTitleBarViewHelper.onDestroy();
        }
    }
}
