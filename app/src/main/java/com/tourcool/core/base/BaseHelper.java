package com.tourcool.core.base;

import android.app.Activity;

import com.aries.ui.util.ResourceUtil;

import org.simple.eventbus.EventBus;

import butterknife.Unbinder;

/**
 * @Author: JenkinsZhou on 2018/11/19 14:13
 * @E-Mail: 971613168@qq.com
 * @Function:
 * @Description:
 */
public class BaseHelper {
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected ResourceUtil mResourceUtil;

    public BaseHelper(Activity context) {
        mContext = context;
        mResourceUtil = new ResourceUtil(mContext);
    }

    /**
     * Activity 关闭onDestroy调用
     */
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

}
