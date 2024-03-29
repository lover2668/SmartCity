package com.tourcool.core.impl;

import android.app.Activity;

import com.frame.library.core.UiManager;
import com.tourcool.core.MyApplication;
import com.frame.library.core.control.SwipeBackControl;
import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.tourcool.smartcity.R;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackLayout;

/**
 * @Author: JenkinsZhou on 2018/12/4 18:00
 * @E-Mail: 971613168@qq.com
 * @Function: 滑动返回处理
 * @Description:
 */
public class SwipeBackControlImpl implements SwipeBackControl {

    /**
     * 设置当前Activity是否支持滑动返回(用于控制是否添加一层{@link BGASwipeBackLayout})
     * 返回为true {@link #setSwipeBack(Activity, BGASwipeBackHelper)}才有设置的意义
     *
     * @param activity
     * @return
     */
    @Override
    public boolean isSwipeBackEnable(Activity activity) {
        return true;
    }

    /**
     * 设置Activity 全局滑动属性--包括三方库
     *
     * @param activity
     * @param swipeBackHelper BGASwipeBackHelper 控制详见{@link UiManager}
     */
    @Override
    public void setSwipeBack(Activity activity, BGASwipeBackHelper swipeBackHelper) {
        //以下为默认设置
        //需设置activity window背景为透明避免滑动过程中漏出背景也可减少背景层级降低过度绘制
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        swipeBackHelper.setSwipeBackEnable(true)
                .setShadowResId(R.drawable.bga_sbl_shadow)
                //底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
                .setIsNavigationBarOverlap(MyApplication.isControlNavigation() && NavigationBarUtil.isNavigationAtBottom(activity))
                .setIsShadowAlphaGradient(true);
    }

    @Override
    public void onSwipeBackLayoutSlide(Activity activity, float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel(Activity activity) {

    }

    @Override
    public void onSwipeBackLayoutExecuted(Activity activity) {

    }
}
