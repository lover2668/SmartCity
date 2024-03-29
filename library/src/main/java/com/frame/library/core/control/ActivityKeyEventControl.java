package com.frame.library.core.control;

import android.app.Activity;
import android.view.KeyEvent;

import com.frame.library.core.basis.BaseActivity;

/**
 * @Author: JenkinsZhou on 2018/9/26 16:19
 * @E-Mail: 971613168@qq.com
 * @Function: Activity 按键监控(如音量键、返回键、菜单键)--必须继承自{@link BaseActivity}
 * Activity 必须在前台
 * @Description:
 */
public interface ActivityKeyEventControl {

    /**
     * 按下事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyDown(Activity activity, int keyCode, KeyEvent event);

    /**
     * 抬起事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyUp(Activity activity, int keyCode, KeyEvent event);

    /**
     * 长按事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event);

    /**
     * 键盘快捷键事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyShortcut(Activity activity, int keyCode, KeyEvent event);

    /**
     * 多个连续的重复事件
     *
     * @param activity
     * @param keyCode
     * @param repeatCount
     * @param event
     * @return
     */
    boolean onKeyMultiple(Activity activity, int keyCode, int repeatCount, KeyEvent event);
}
