package com.frame.library.core.control;

import android.view.View;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/20 17:08
 * @E-Mail: 971613168@qq.com
 * Function: StatusLayoutManager 属性控制
 * Description:
 */
public interface IMultiStatusView {
    /**
     * 设置StatusLayoutManager 的目标View
     *
     * @return
     */
    default View getMultiStatusContentView() {
        return null;
    }

    /**
     * 设置StatusLayoutManager属性
     *
     * @param statusView
     */
    default void setMultiStatusView(StatusLayoutManager.Builder statusView) {

    }

    /**
     * 设置StatusLayoutManager
     *
     * @param manager
     */
    default void setMultiStatusView(StatusLayoutManager manager) {
    }

    /**
     * 获取空布局里点击View回调
     *
     * @return
     */
    default View.OnClickListener getEmptyClickListener() {
        return null;
    }

    /**
     * 获取错误布局里点击View回调
     *
     * @return
     */
    default View.OnClickListener getErrorClickListener() {
        return null;
    }

    /**
     * 获取自定义布局里点击View回调
     *
     * @return
     */
    default View.OnClickListener getCustomerClickListener() {
        return null;
    }
}
