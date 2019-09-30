package com.tourcool.adapter.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.adapter.TwoLevelChildAdapter;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月26日14:44
 * @Email: 971613168@qq.com
 */
public class HomeSecondListAdapter extends BaseQuickAdapter<HomeChildBean, BaseViewHolder> {

    public HomeSecondListAdapter() {
        super(R.layout.item_two_level_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildBean item) {
        helper.setText(R.id.tvGroupName, TourCooUtil.getNotNullValue(item.getTitle()));
        RecyclerView childRecyclerView = helper.getView(R.id.rvCommonChild);
        TwoLevelChildAdapter adapter = new TwoLevelChildAdapter();
        //二级布局为网格布局
        childRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter.bindToRecyclerView(childRecyclerView);
        adapter.setNewData(item.getChildList());


    }

}
