package com.frame.library.core.control;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: JenkinsZhou on 2018/7/20 16:52
 * @E-Mail: 971613168@qq.com
 * Function: 下拉及上拉刷新
 * Description:
 * 1、2018-7-20 17:11:22 多状态集成关系
 * 2、2018-7-20 17:39:55 去掉点击事件getMultiStatusViewChildClickListener
 * 3、2019-3-22 15:06:07 抽离下拉刷新功能并设置部分默认返回值
 */
public interface IFrameRefreshLoadView<T> extends IFrameRefreshView, BaseQuickAdapter.RequestLoadMoreListener, IMultiStatusView {
    /**
     * 使用BaseRecyclerViewAdapterHelper作为上拉加载的实现方式
     * 如果使用ListView或GridView等需要自己去实现上拉加载更多的逻辑
     *
     * @return BaseRecyclerViewAdapterHelper的实现类
     */
    BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    /**
     * 获取RecyclerView的布局管理器对象，根据自己业务实际情况返回
     *
     * @return RecyclerView的布局管理器对象
     */
    default RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    /**
     * 获取加载更多布局
     *
     * @return
     */
    default LoadMoreView getLoadMoreView() {
        return null;
    }

    /**
     * 触发下拉或上拉刷新操作
     *
     * @param page
     */
    void loadData(int page);

    /**
     * 是否支持加载更多功能
     *
     * @return
     */
    default boolean isLoadMoreEnable() {
        return true;
    }

    /**
     * item是否有点击事件
     *
     * @return
     */
    default boolean isItemClickEnable() {
        return true;
    }

    /**
     * item点击回调
     *
     * @param adapter
     * @param view
     * @param position
     */
    default void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position) {

    }

    /**
     * 设置全局监听接口
     *
     * @return
     */
    IHttpRequestControl getIHttpRequestControl();
}
