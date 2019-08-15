package com.tourcool.core.touch;

/**
 * @Author: JenkinsZhou on 2018/8/9 17:25
 * @E-Mail: 971613168@qq.com
 * Function: ViewHolder实现接口
 * Description:
 */
public interface ItemTouchHelperViewHolder {

    /**
     * item被选中,在侧滑或拖拽过程中更新状态
     */
    void onItemSelectedChanged();

    /**
     * item的拖拽或侧滑结束，恢复默认的状态
     */
    void onItemClear();
}
