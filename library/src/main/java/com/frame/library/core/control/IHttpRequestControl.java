package com.frame.library.core.control;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @Author: JenkinsZhou on 2018/7/23 10:43
 * @E-Mail: 971613168@qq.com
 * Function: 基于实现{@link IFrameRefreshLoadView}下拉刷新、列表、多状态布局的全局回调接口
 * Description:
 */
public interface IHttpRequestControl {

    /**
     * 获取刷新布局
     *
     * @return
     */
    SmartRefreshLayout getRefreshLayout();

    /**
     * 获取RecyclerView Adapter
     *
     * @return
     */
    BaseQuickAdapter getRecyclerAdapter();

    /**
     * 获取多布局状态管理
     *
     * @return
     */
    StatusLayoutManager getStatusLayoutManager();

    /**
     * 获取当前页码
     * @return
     */
    int getCurrentPage();

    /**
     * 获取每页数量
     * @return
     */
    int getPageSize();

    /**
     * 获取调用类
     * @return
     */
    Class<?> getRequestClass();

}
