package com.frame.library.core;

import android.app.Activity;

import com.frame.library.core.manager.LoggerManager;

import org.simple.eventbus.EventBus;

import butterknife.Unbinder;

/**
 * @Author: JenkinsZhou on 2019/8/7 14:22
 * @E-Mail: 971613168@qq.com
 * @Function: 绑定Activity Helper
 * @Description:
 */
public class BaeHelper {
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected String mTag = getClass().getSimpleName();

    public BaeHelper(Activity context) {
        mContext = context;
        DelegateManager.getInstance().putBasisHelper(context, this);
    }

    /**
     * Activity 关闭onDestroy调用
     */
    public void onDestroy() {
        LoggerManager.i(mTag, "onDestroy");
        EventBus.getDefault().unregister(this);
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }
}
