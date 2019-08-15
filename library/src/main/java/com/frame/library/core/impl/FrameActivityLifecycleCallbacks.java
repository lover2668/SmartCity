package com.frame.library.core.impl;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @Author: JenkinsZhou on 2018/7/30 10:12
 * @E-Mail: 971613168@qq.com
 * Function: 快速Application 生命周期回调 {@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)}
 * Description:
 */
public class FrameActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
