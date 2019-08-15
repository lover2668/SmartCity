package com.frame.library.core;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.frame.library.core.delegate.FrameRefreshDelegate;
import com.frame.library.core.delegate.FrameTitleDelegate;
import com.frame.library.core.manager.LoggerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @Author: JenkinsZhou on 2019/3/25 10:48
 * @E-Mail: 971613168@qq.com
 * @Function: 保存Delegate对象以便统一销毁
 * @Description:
 */
class DelegateManager {

    private static volatile DelegateManager sInstance;

    private DelegateManager() {
    }

    public static DelegateManager getInstance() {
        if (sInstance == null) {
            synchronized (DelegateManager.class) {
                if (sInstance == null) {
                    sInstance = new DelegateManager();
                }
            }
        }
        return sInstance;
    }


    /**
     * 装载FastRefreshDelegate Map对象
     */
    private WeakHashMap<Class, FrameRefreshDelegate> mFastRefreshDelegateMap = new WeakHashMap<>();
    private WeakHashMap<Class, FrameTitleDelegate> mFastTitleDelegateMap = new WeakHashMap<>();
    private WeakHashMap<Activity, List<BaeHelper>> mBasisHelperMap = new WeakHashMap<>();

    public FrameRefreshDelegate getFastRefreshDelegate(Class cls) {
        FrameRefreshDelegate delegate = null;
        if (cls != null && mFastRefreshDelegateMap.containsKey(cls)) {
            delegate = mFastRefreshDelegateMap.get(cls);
        }
        return delegate;
    }

    public void putFastRefreshDelegate(Class cls, FrameRefreshDelegate delegate) {
        if (cls != null && !mFastRefreshDelegateMap.containsKey(cls)) {
            mFastRefreshDelegateMap.put(cls, delegate);
        }
    }

    /**
     * {@link FrameLifecycleCallbacks#onFragmentViewDestroyed(FragmentManager, Fragment)}
     * {@link FrameLifecycleCallbacks#onActivityDestroyed(Activity)}
     *
     * @param cls
     */
    public void removeFastRefreshDelegate(Class cls) {
        FrameRefreshDelegate delegate = getFastRefreshDelegate(cls);
        LoggerManager.i("removeFastRefreshDelegate_class:" + cls + ";delegate:" + delegate);
        if (delegate != null) {
            delegate.onDestroy();
            mFastRefreshDelegateMap.remove(cls);
        }
    }

    public FrameTitleDelegate getFastTitleDelegate(Class cls) {
        FrameTitleDelegate delegate = null;
        if (cls != null && mFastTitleDelegateMap.containsKey(cls)) {
            delegate = mFastTitleDelegateMap.get(cls);
        }
        return delegate;
    }

    public void putFastTitleDelegate(Class cls, FrameTitleDelegate delegate) {
        if (cls != null && !mFastTitleDelegateMap.containsKey(cls)) {
            mFastTitleDelegateMap.put(cls, delegate);
        }
    }

    /**
     * {@link FrameLifecycleCallbacks#onFragmentViewDestroyed(FragmentManager, Fragment)}
     * {@link FrameLifecycleCallbacks#onActivityDestroyed(Activity)}
     *
     * @param cls
     */
    public void removeFastTitleDelegate(Class cls) {
        FrameTitleDelegate delegate = getFastTitleDelegate(cls);
        if (delegate != null) {
            delegate.onDestroy();
            mFastTitleDelegateMap.remove(cls);
        }
    }


    public void putBasisHelper(Activity activity, BaeHelper helper) {
        if (activity == null) {
            return;
        }
        if (mBasisHelperMap.containsKey(activity)) {
            mBasisHelperMap.get(activity).add(helper);
        } else {
            List<BaeHelper> list = new ArrayList<>();
            list.add(helper);
            mBasisHelperMap.put(activity, list);
        }
    }

    /**
     * {@link FrameLifecycleCallbacks#onActivityDestroyed(Activity)}
     *
     * @param activity
     */
    public void removeBasisHelper(Activity activity) {
        if (mBasisHelperMap.containsKey(activity)) {
            List<BaeHelper> list = mBasisHelperMap.get(activity);
            if (list != null) {
                LoggerManager.i("list:"+list.size());
                for (BaeHelper item : list) {
                    item.onDestroy();
                }
                list.clear();
                mBasisHelperMap.remove(activity);
            }
        }
    }
}
