package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.library.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :应用管理适配器
 * @company :途酷科技
 * @date 2019年08月28日14:27
 * @Email: 971613168@qq.com
 */
public class ModuleListAdapter extends BaseQuickAdapter<HomeChildBean, BaseViewHolder> {
    private List<HomeChildItem> myItemList;

    public ModuleListAdapter(List<HomeChildItem> homeChildItemList) {
        super(R.layout.item_module_group_layout);
        this.myItemList = homeChildItemList;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildBean item) {
        helper.setText(R.id.tvGroupName, item.getTitle());
        RecyclerView rvModuleGrid = helper.getView(R.id.rvModuleGrid);
        rvModuleGrid.setLayoutManager(new GridLayoutManager(mContext, 4));
        ModuleManagerAdapter gridAdapter = new ModuleManagerAdapter(myItemList);
        gridAdapter.bindToRecyclerView(rvModuleGrid);
        gridAdapter.setNewData(item.getChildList());
    }

    public void loadHeader() {
        if (myItemList == null) {
            return;
        }
        HomeChildBean header = new HomeChildBean();
        List<HomeChildItem> copyList = new ArrayList<>();
        for (HomeChildItem homeChildItem : myItemList) {
            if (homeChildItem == null) {
                continue;
            }
            HomeChildItem copyItem = homeChildItem.copy();
            if (copyItem == null) {
                continue;
            }
            copyItem.setParentGroup(false);
            copyList.add(copyItem);
        }
        header.setTitle("我的应用");
        header.setChildList(copyList);
        getData().add(0, header);
        notifyDataSetChanged();
    }


    public void setNewData(List<HomeChildBean> data) {
        super.setNewData(data);
        loadHeader();
    }

}
