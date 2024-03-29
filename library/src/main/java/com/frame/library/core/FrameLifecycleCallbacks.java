package com.frame.library.core;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.helper.status.StatusViewHelper;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.tab.CommonTabLayout;
import com.frame.library.core.control.ActivityFragmentControl;
import com.frame.library.core.control.IFrameRefreshLoadView;
import com.frame.library.core.control.IFrameRefreshView;
import com.frame.library.core.control.IFrameTitleView;
import com.frame.library.core.control.INavigationBar;
import com.frame.library.core.control.IStatusBar;
import com.frame.library.core.control.SwipeBackControl;
import com.frame.library.core.delegate.FrameRefreshDelegate;
import com.frame.library.core.delegate.FrameTitleDelegate;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.module.activity.FrameMainActivity;
import com.frame.library.core.module.activity.FrameRefreshLoadActivity;
import com.frame.library.core.module.activity.FrameWebActivity;
import com.frame.library.core.module.fragment.BaseRefreshLoadFragment;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.util.FrameUtil;
import com.frame.library.core.util.StackUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.library.frame.demo.R;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @Author: JenkinsZhou on 2018/7/30 13:48
 * @E-Mail: 971613168@qq.com
 * Function: Activity/Fragment生命周期
 * Description:
 * 1、2018-7-2 09:29:54 新增继承{@link FrameMainActivity}的Activity虚拟导航栏功能
 * 2、2018-11-29 11:49:46 {@link #setStatusBar(Activity)}增加topView background 空判断
 * 3、2018-11-29 11:50:58 {@link #onActivityDestroyed(Activity)} 出栈方法调用{@link StackUtil#pop(Activity, boolean)} 第二个参数设置为false避免因Activity状态切换进入生命周期造成状态无法保存问题
 * 4、2019-3-25 14:27:33 新增下拉刷新功能处理及管理并删除原Fragment生命周期注销方法
 * 5、2019-4-8 17:03:25 删除关于{@link IFrameRefreshLoadView}与{@link IFrameRefreshView}重复判断相关代码
 */
public class FrameLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String TAG = getClass().getSimpleName();
    private ActivityFragmentControl mActivityFragmentControl;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks;
    private SwipeBackControl mSwipeBackControl;


    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        LoggerManager.i(TAG, "onActivityCreated:" + activity.getClass().getSimpleName() + ";contentView:" + FrameUtil.getRootView(activity));
        getControl();

        //统一Activity堆栈管理
        StackUtil.getInstance().push(activity);
        //统一横竖屏操作
        if (mActivityFragmentControl != null) {
            mActivityFragmentControl.setRequestedOrientation(activity);
        }
        //统一Fragment生命周期处理
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            fragmentManager.registerFragmentLifecycleCallbacks(this, true);
            if (mFragmentLifecycleCallbacks != null) {
                fragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, true);
            }
        }
        //设置滑动返回
        if (!(activity instanceof BGASwipeBackHelper.Delegate)) {
            setSwipeBack(activity);
        }
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        View contentView = FrameUtil.getRootView(activity);
        LoggerManager.i(TAG, "onActivityStarted:" + activity.getClass().getSimpleName() + ";contentView:" + contentView);
        boolean isSet = activity.getIntent().getBooleanExtra(GlobalConstant.IS_SET_CONTENT_VIEW_BACKGROUND, false);
        if (!isSet) {
            setContentViewBackground(FrameUtil.getRootView(activity), activity.getClass());
        }
        //设置状态栏
        setStatusBar(activity);
        //设置虚拟导航栏功能
//        setNavigationBar(activity);
        //设置TitleBarView-先设置TitleBarView避免多状态将布局替换
        if (activity instanceof IFrameTitleView
                && !(activity instanceof IFrameRefreshLoadView)
                && !activity.getIntent().getBooleanExtra(GlobalConstant.IS_SET_TITLE_BAR_VIEW, false)
                && contentView != null) {
            DelegateManager.getInstance().putFastTitleDelegate(activity.getClass(),
                    new FrameTitleDelegate(contentView, (IFrameTitleView) activity, activity.getClass()));
            activity.getIntent().putExtra(GlobalConstant.IS_SET_TITLE_BAR_VIEW, true);
        }
        //配置下拉刷新
        if (activity instanceof IFrameRefreshView
                && !(FrameRefreshLoadActivity.class.isAssignableFrom(activity.getClass()))
                && !activity.getIntent().getBooleanExtra(GlobalConstant.IS_SET_REFRESH_VIEW, false)) {
            IFrameRefreshView refreshView = (IFrameRefreshView) activity;
            if (contentView != null
                    || refreshView.getContentView() != null) {
                DelegateManager.getInstance().putFastRefreshDelegate(activity.getClass(),
                        new FrameRefreshDelegate(
                                refreshView.getContentView() != null ? refreshView.getContentView() : contentView,
                                (IFrameRefreshView) activity));
                activity.getIntent().putExtra(GlobalConstant.IS_SET_REFRESH_VIEW, true);
            }
        }
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LoggerManager.i(TAG, "onActivityResumed:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LoggerManager.i(TAG, "onActivityPaused:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        //Activity销毁前的时机需要关闭软键盘-在onActivityStopped及onActivityDestroyed生命周期内已无法关闭
        if (activity.isFinishing()) {
            KeyboardHelper.closeKeyboard(activity);
        }
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LoggerManager.i(TAG, "onActivityStopped:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LoggerManager.i(TAG, "onActivitySaveInstanceState:" + activity.getClass().getSimpleName());
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //横竖屏会重绘将状态重置
        if (activity.getIntent() != null) {
            activity.getIntent().removeExtra(GlobalConstant.IS_SET_STATUS_VIEW_HELPER);
            activity.getIntent().removeExtra(GlobalConstant.IS_SET_NAVIGATION_VIEW_HELPER);
            activity.getIntent().removeExtra(GlobalConstant.IS_SET_CONTENT_VIEW_BACKGROUND);
            activity.getIntent().removeExtra(GlobalConstant.IS_SET_REFRESH_VIEW);
            activity.getIntent().removeExtra(GlobalConstant.IS_SET_TITLE_BAR_VIEW);
        }
        LoggerManager.i(TAG, "onActivityDestroyed:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        StackUtil.getInstance().pop(activity, false);

        //清除下拉刷新代理FastRefreshDelegate
        DelegateManager.getInstance().removeFastRefreshDelegate(activity.getClass());
        //清除标题栏代理类FastTitleDelegate
        DelegateManager.getInstance().removeFastTitleDelegate(activity.getClass());
        //清除BasisHelper
        DelegateManager.getInstance().removeBasisHelper(activity);
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityDestroyed(activity);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        boolean isSet = f.getArguments() != null ? f.getArguments().getBoolean(GlobalConstant.IS_SET_CONTENT_VIEW_BACKGROUND, false) : false;
        if (!isSet) {
            setContentViewBackground(v, f.getClass());
        }
        //设置TitleBarView-先设置TitleBarView避免多状态将布局替换
        if (f instanceof IFrameTitleView
                && !(f instanceof IFrameRefreshLoadView)
                && v != null) {
            DelegateManager.getInstance().putFastTitleDelegate(f.getClass(),
                    new FrameTitleDelegate(v, (IFrameTitleView) f, f.getClass()));
        }
        //刷新功能处理
        if (f instanceof IFrameRefreshView
                && !(BaseRefreshLoadFragment.class.isAssignableFrom(f.getClass()))) {
            IFrameRefreshView refreshView = (IFrameRefreshView) f;
            DelegateManager.getInstance().putFastRefreshDelegate(f.getClass(),
                    new FrameRefreshDelegate(
                            refreshView.getContentView() != null ? refreshView.getContentView() : f.getView(),
                            refreshView));
        }
    }

    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if (f.getArguments() != null) {
            f.getArguments().putBoolean(GlobalConstant.IS_SET_CONTENT_VIEW_BACKGROUND, false);
        }
        DelegateManager.getInstance().removeFastRefreshDelegate(f.getClass());
        DelegateManager.getInstance().removeFastTitleDelegate(f.getClass());
    }

    /**
     * 实时获取回调
     */

    private void getControl() {
        mSwipeBackControl = UiManager.getInstance().getSwipeBackControl();
        mActivityFragmentControl = UiManager.getInstance().getActivityFragmentControl();
        if (mActivityFragmentControl == null) {
            return;
        }
        mActivityLifecycleCallbacks = mActivityFragmentControl.getActivityLifecycleCallbacks();
        mFragmentLifecycleCallbacks = mActivityFragmentControl.getFragmentLifecycleCallbacks();
    }

    /**
     * 回调设置Activity/Fragment背景
     *
     * @param v
     * @param cls
     */
    private void setContentViewBackground(View v, Class<?> cls) {
        if (mActivityFragmentControl != null && v != null) {
            mActivityFragmentControl.setContentViewBackground(v, cls);
        }
    }

    /**
     * 设置滑动返回相关
     *
     * @param activity
     */
    private void setSwipeBack(final Activity activity) {
        LoggerManager.i(TAG, activity + getClass().getSimpleName() + ":设置Activity滑动返回");
        //需设置activity window背景为透明避免滑动过程中漏出背景也可减少背景层级降低过度绘制
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        BGASwipeBackHelper swipeBackHelper = new BGASwipeBackHelper(activity, new BGASwipeBackHelper.Delegate() {
            @Override
            public boolean isSupportSwipeBack() {
                return mSwipeBackControl == null || mSwipeBackControl.isSwipeBackEnable(activity);
            }

            @Override
            public void onSwipeBackLayoutSlide(float slideOffset) {
                LoggerManager.i(TAG, "onSwipeBackLayoutCancel");
                Activity pre = StackUtil.getInstance().getPrevious();
                if (pre != null && pre instanceof FrameWebActivity) {
                    ((FrameWebActivity) pre).onWebViewResume();
                }
                if (mSwipeBackControl != null) {
                    mSwipeBackControl.onSwipeBackLayoutSlide(activity, slideOffset);
                }
            }


            @Override
            public void onSwipeBackLayoutCancel() {
                LoggerManager.i(TAG, "onSwipeBackLayoutCancel");
                Activity pre = StackUtil.getInstance().getPrevious();
                if (pre != null && pre instanceof FrameWebActivity) {
                    ((FrameWebActivity) pre).onWebViewPause();
                }
                if (mSwipeBackControl != null) {
                    mSwipeBackControl.onSwipeBackLayoutCancel(activity);
                }
            }

            @Override
            public void onSwipeBackLayoutExecuted() {
                //设置退出动画-确保效果准确
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                KeyboardHelper.closeKeyboard(activity);
                activity.finish();
                activity.overridePendingTransition(0, R.anim.bga_sbl_activity_swipeback_exit);
                if (mSwipeBackControl != null) {
                    mSwipeBackControl.onSwipeBackLayoutExecuted(activity);
                }
            }
        })
                //设置滑动背景
                .setShadowResId(R.drawable.bga_sbl_shadow)
                //底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
                .setIsNavigationBarOverlap(true)
                .setIsShadowAlphaGradient(true);
        //用于全局控制
        if (mSwipeBackControl != null) {
            mSwipeBackControl.setSwipeBack(activity, swipeBackHelper);
        }
    }

    /**
     * 设置状态栏
     *
     * @param activity 目标Activity
     */
    private void setStatusBar(Activity activity) {
        boolean isSet = activity.getIntent().getBooleanExtra(GlobalConstant.IS_SET_STATUS_VIEW_HELPER, false);
        if (isSet) {
            return;
        }
        TitleBarView titleBarView = FindViewUtil.getTargetView(activity.getWindow().getDecorView(), TitleBarView.class);
        //不包含TitleBarView处理
        if (titleBarView == null && !(activity instanceof FrameMainActivity)) {
            View topView = getTopView(FrameUtil.getRootView(activity));
            LoggerManager.i(TAG, "其它三方库设置状态栏沉浸");
            StatusViewHelper statusViewHelper = StatusViewHelper.with(activity)
                    .setControlEnable(true)
                    .setPlusStatusViewEnable(false)
                    .setTransEnable(true)
                    .setTopView(topView);
            if (topView != null && topView.getBackground() != null) {
                Drawable drawable = topView.getBackground().mutate();
                statusViewHelper.setStatusLayoutDrawable(drawable);
            }
            boolean isInit = mActivityFragmentControl != null ? mActivityFragmentControl.setStatusBar(activity, statusViewHelper, topView) : true;
            if (activity instanceof IStatusBar) {
                isInit = ((IStatusBar) activity).setStatusBar(activity, statusViewHelper, topView);
            }
            if (isInit) {
                //状态栏黑色文字图标flag被覆盖问题--临时解决
                RxJavaManager.getInstance().setTimer(10)
                        .subscribe(new BaseObserver<Long>() {
                            @Override
                            public void onRequestNext(Long entity) {
                                if (activity == null || activity.isFinishing()) {
                                    return;
                                }
                                statusViewHelper.init();
                                activity.getIntent().putExtra(GlobalConstant.IS_SET_STATUS_VIEW_HELPER, true);
                            }
                        });
            }
        }
    }

    /**
     * 设置全局虚拟导航栏功能
     *
     * @param activity 目标Activity
     */
    private void setNavigationBar(Activity activity) {
        boolean isSet = activity.getIntent().getBooleanExtra(GlobalConstant.IS_SET_NAVIGATION_VIEW_HELPER, false);
        if (isSet) {
            return;
        }
        LoggerManager.i(TAG, "setNavigationBars:设置虚拟导航栏");
        View bottomView = FrameUtil.getRootView(activity);
        //继承FastMainActivity底部View处理
        if (FrameMainActivity.class.isAssignableFrom(activity.getClass())) {
            CommonTabLayout tabLayout = FindViewUtil.getTargetView(bottomView, CommonTabLayout.class);
            if (tabLayout != null) {
                bottomView = tabLayout;
            }
        }
        //设置虚拟导航栏控制
        NavigationViewHelper navigationViewHelper = NavigationViewHelper.with(activity)
               .setWhiteStyle();
        if (activity instanceof KeyboardHelper.OnKeyboardVisibilityChangedListener) {
            navigationViewHelper.setOnKeyboardVisibilityChangedListener((KeyboardHelper.OnKeyboardVisibilityChangedListener) activity);
        }
        boolean isInit = mActivityFragmentControl != null ? mActivityFragmentControl.setNavigationBar(activity, navigationViewHelper, bottomView) : true;
        if (activity instanceof INavigationBar) {
            isInit = ((INavigationBar) activity).setNavigationBar(activity, navigationViewHelper, bottomView);
        }
        if (isInit) {
            activity.getIntent().putExtra(GlobalConstant.IS_SET_NAVIGATION_VIEW_HELPER, true);
            navigationViewHelper.init();
        }
    }

    /**
     * 获取Activity 顶部View(用于延伸至状态栏下边)
     *
     * @param target
     * @return
     */
    private View getTopView(View target) {
        if (target != null && target instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) target;
            if (group.getChildCount() > 0) {
                target = ((ViewGroup) target).getChildAt(0);
            }
        }
        return target;
    }
}
