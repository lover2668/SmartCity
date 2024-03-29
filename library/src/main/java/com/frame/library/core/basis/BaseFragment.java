package com.frame.library.core.basis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.LogUtils;
import com.frame.library.core.UiManager;
import com.frame.library.core.control.IBaseView;
import com.frame.library.core.control.IFrameRefreshLoadView;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.widget.dialog.FrameLoadingDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.simple.eventbus.EventBus;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: JenkinsZhou on 2018/7/13 17:49
 * @E-Mail: 971613168@qq.com
 * Function: 所有Fragment的基类实现懒加载
 * Description:
 * 1、新增控制是否为FragmentActivity的唯一Fragment 方法以优化懒加载方式
 * 2、增加解决StatusLayoutManager与SmartRefreshLayout冲突解决方案
 * 3、2018-7-6 17:12:16 删除IBasisFragment 控制是否单Fragment 通过另一种方式实现
 * 4、2019-1-29 18:33:10 修改对用户可以见回调{@link #setUserVisibleHint(boolean)}{@link #onHiddenChanged(boolean)} (boolean)}
 */
public abstract class BaseFragment extends RxFragment implements IBaseView {

    private Handler mRootHandler = new Handler();
    protected FrameLoadingDialog loadingDialog;
    protected Activity mContext;
    protected View mContentView;
    protected boolean mIsFirstShow;
    protected boolean mIsViewLoaded;
    protected Unbinder mUnBinder;
    protected String TAG = getClass().getSimpleName();
    protected boolean mIsVisibleChanged = false;
    private boolean mIsInViewPager;
    protected Bundle mSavedInstanceState;

    /**
     * 检查Fragment或FragmentActivity承载的Fragment是否只有一个
     *
     * @return
     */
    protected boolean isSingleFragment() {
        int size = 0;
        FragmentManager manager = getFragmentManager();
        if (manager != null && manager.getFragments() != null) {
            size = manager.getFragments().size();
        }
        LoggerManager.i(TAG, TAG + ";FragmentManager承载Fragment数量:" + size);
        return size <= 1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        mIsFirstShow = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        beforeSetContentView();
        mContentView = inflater.inflate(getContentLayout(), container, false);
        //解决StatusLayoutManager与SmartRefreshLayout冲突
        if (this instanceof IFrameRefreshLoadView) {
            if (FrameUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                if (mContentView.getClass() == SmartRefreshLayout.class) {
                    FrameLayout frameLayout = new FrameLayout(mContext);
                    if (mContentView.getLayoutParams() != null) {
                        frameLayout.setLayoutParams(mContentView.getLayoutParams());
                    }
                    frameLayout.addView(mContentView);
                    mContentView = frameLayout;
                }
            }
        }
        mUnBinder = ButterKnife.bind(this, mContentView);
        mIsViewLoaded = true;
        if (isEventBusEnable()) {
            EventBus.getDefault().register(this);
        }
        beforeInitView(savedInstanceState);
        initView(savedInstanceState);

        if (isSingleFragment() && !mIsVisibleChanged) {
            if (getUserVisibleHint() || isVisible() || !isHidden()) {
                onVisibleChanged(true);
            }
        }
        LoggerManager.i(TAG, TAG + ";mIsVisibleChanged:" + mIsVisibleChanged
                + ";getUserVisibleHint:" + getUserVisibleHint()
                + ";isHidden:" + isHidden() + ";isVisible:" + isVisible());
        loadingDialog = new FrameLoadingDialog(mContext, "加载中...");
        return mContentView;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        if (UiManager.getInstance().getActivityFragmentControl() != null) {
            UiManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        mUnBinder = null;
        mContentView = null;
        mContext = null;
        mSavedInstanceState = null;
        TAG = null;
    }

    @Override
    public void onDestroy() {
        if (isEventBusEnable()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoggerManager.i(TAG, "onResume-isAdded:" + isAdded() + ";getUserVisibleHint:" + getUserVisibleHint()
                + ";isHidden:" + isHidden() + ";isVisible:" + isVisible() + ";isResume:" + isResumed() + ";isVisibleToUser:" + isVisibleToUser(this) + ";host:");
        if (isAdded() && isVisibleToUser(this)) {
            onVisibleChanged(true);
        }
    }

    /**
     * @param fragment
     * @return
     */
    private boolean isVisibleToUser(BaseFragment fragment) {
        if (fragment == null) {
            return false;
        }
        if (fragment.getParentFragment() != null) {
            return isVisibleToUser((BaseFragment) fragment.getParentFragment()) && (fragment.isInViewPager() ? fragment.getUserVisibleHint() : fragment.isVisible());
        }
        return fragment.isInViewPager() ? fragment.getUserVisibleHint() : fragment.isVisible();
    }

    /**
     * 不在viewpager中Fragment懒加载
     */
    @Override
    public void onHiddenChanged(final boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!mIsViewLoaded) {
            RxJavaManager.getInstance().setTimer(10)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onRequestNext(Long entity) {
                            onHiddenChanged(hidden);
                        }
                    });
        } else {
            onVisibleChanged(!hidden);
        }

    }

    /**
     * 在viewpager中的Fragment懒加载
     */
    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsInViewPager = true;
        if (!mIsViewLoaded) {
            RxJavaManager.getInstance().setTimer(10)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onRequestNext(Long entity) {
                            setUserVisibleHint(isVisibleToUser);
                        }
                    });
        } else {
            onVisibleChanged(isVisibleToUser);
        }
    }

    /**
     * 是否在ViewPager
     *
     * @return
     */
    public boolean isInViewPager() {
        return mIsInViewPager;
    }

    /**
     * 用户可见变化回调
     *
     * @param isVisibleToUser
     */
    protected void onVisibleChanged(final boolean isVisibleToUser) {
        LoggerManager.i(TAG, "onVisibleChanged-isVisibleToUser:" + isVisibleToUser);
        mIsVisibleChanged = true;
        if (isVisibleToUser) {
            //避免因视图未加载子类刷新UI抛出异常
            if (!mIsViewLoaded) {
                RxJavaManager.getInstance().setTimer(10)
                        .compose(bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new BaseObserver<Long>() {
                            @Override
                            public void onRequestNext(Long entity) {
                                onVisibleChanged(true);
                            }
                        });
            } else {
                fastLazyLoad();
            }
        }
    }

    private void fastLazyLoad() {
        if (mIsFirstShow && mIsViewLoaded) {
            mIsFirstShow = false;
            loadData();
        }
    }


    protected void setStatusBarModeWhite(RxFragment fragment) {
        setStatusBar(fragment, false);
    }

    protected void setStatusBarModeDark(RxFragment fragment) {
        setStatusBar(fragment, true);
    }
    protected void setStatusBarColor(RxFragment fragment,int color) {
        if(color <= 0){
            return;
        }
        mRootHandler.postDelayed(() -> ImmersionBar.with(fragment)
                .statusBarColor(color)
                .init(), 50);
    }

    private void setStatusBar(RxFragment fragment, boolean isDarkFont) {
        mRootHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImmersionBar.with(fragment)
                        .statusBarDarkFont(isDarkFont, 0.2f)
                        .init();
            }
        }, 1);
    }

    protected <T> T parseJavaBean(Object data, Class<T> tClass) {
        try {
            return JSON.parseObject(JSON.toJSONString(data), tClass);
        } catch (Exception e) {
            TourCooLogUtil.e("parseJavaBean()报错--->" + e.toString());
            return null;
        }
    }





    protected <T> List<T> parseJsonToBeanList(Object data, Class<T> model) {
        try {
            return JSONArray.parseArray(JSON.toJSONString(data), model);
        } catch (Exception e) {
              TourCooLogUtil.e(TAG,"parseJsonToBeanList()报错--->"+e.toString());
            return null;
        }
    }



    protected void setViewVisible(View view, boolean visible) {
        if (view == null) {
            TourCooLogUtil.e(TAG, "setViewVisible()--->View==null！");
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setViewGone(View view, boolean visible) {
        if (view == null) {
            TourCooLogUtil.e(TAG, "setViewGone()--->View==null！");
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }



}
