package com.frame.library.core.control;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/20 16:51
 * @E-Mail: 971613168@qq.com
 * Function: 用于全局设置多状态布局
 * Description:
 * 1、修改设置多状态布局方式
 */
public interface MultiStatusView {

    /**
     * 设置多状态布局属性
     *
     * @param statusView
     * @param iFastRefreshLoadView
     */
    void setMultiStatusView(StatusLayoutManager.Builder statusView, IFrameRefreshLoadView iFastRefreshLoadView);
}
