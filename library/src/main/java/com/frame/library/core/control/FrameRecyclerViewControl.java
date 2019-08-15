package com.frame.library.core.control;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: JenkinsZhou on 2018/11/19 12:02
 * @E-Mail: 971613168@qq.com
 * @Function: {@link IFrameRefreshLoadView}列表布局全局控制RecyclerView
 * @Description:
 */
public interface FrameRecyclerViewControl {

    /**
     * 全局设置
     *
     * @param recyclerView
     * @param cls
     */
    void setRecyclerView(RecyclerView recyclerView, Class<?> cls);
}
