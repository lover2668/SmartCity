package com.frame.library.core.control;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;

import com.frame.library.core.basis.BaseActivity;

/**
 * @Author: JenkinsZhou on 2018/9/26 16:19
 * @E-Mail: 971613168@qq.com
 * @Function: Activity 事件派发--必须继承自{@link BaseActivity}
 * Activity 必须在前台
 * @Description:
 */
public interface ActivityDispatchEventControl {

    /**
     * {@link BaseActivity#dispatchTouchEvent(MotionEvent)}
     *
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchTouchEvent(Activity activity, MotionEvent event);

    /**
     * {@link BaseActivity#dispatchGenericMotionEvent(MotionEvent)}
     *
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchGenericMotionEvent(Activity activity, MotionEvent event);

    /**
     * {@link BaseActivity#dispatchKeyEvent(KeyEvent)}
     *
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchKeyEvent(Activity activity, KeyEvent event);

    /**
     * {@link BaseActivity#dispatchKeyShortcutEvent(KeyEvent)}
     *
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchKeyShortcutEvent(Activity activity, KeyEvent event);

    /**
     * {@link BaseActivity#dispatchTrackballEvent(MotionEvent)}
     *
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchTrackballEvent(Activity activity, MotionEvent event);

    /**
     * {@link BaseActivity#dispatchPopulateAccessibilityEvent(AccessibilityEvent)}
     *
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchPopulateAccessibilityEvent(Activity activity, AccessibilityEvent event);
}
