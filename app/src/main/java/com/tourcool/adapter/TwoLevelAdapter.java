package com.tourcool.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.library.fast.demo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.TwoLevelBean;
import com.tourcool.core.util.TourCoolUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月20日16:59
 * @Email: 971613168@qq.com
 */
public class TwoLevelAdapter extends BaseQuickAdapter<TwoLevelBean, BaseViewHolder> {

    public TwoLevelAdapter() {
        super(R.layout.item_two_level_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TwoLevelBean item) {
        helper.setText(R.id.tvGroupName, TourCoolUtil.getNotNullValue(item.getGroupName()));
        RecyclerView childRecyclerView = helper.getView(R.id.rvCommonChild);
        TwoLevelChildAdapter adapter = new TwoLevelChildAdapter();
        //二级布局为网格布局
        childRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter.bindToRecyclerView(childRecyclerView);
        adapter.setNewData(item.getChildBeans());


    }
}
