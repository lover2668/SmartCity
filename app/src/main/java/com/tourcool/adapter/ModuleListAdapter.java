package com.tourcool.adapter;


import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.adapter.drag.GridModuleAdapter;
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
    private Handler mHandler = new Handler();

    public ModuleListAdapter(List<HomeChildItem> homeChildItemList) {
        super(R.layout.item_module_group_layout);
        this.myItemList = homeChildItemList;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildBean item) {
        helper.setText(R.id.tvGroupName, item.getTitle());
        RecyclerView rvModuleGrid = helper.getView(R.id.rvModuleGrid);
        rvModuleGrid.setLayoutManager(new GridLayoutManager(mContext, 4));
        GridModuleAdapter gridAdapter = new GridModuleAdapter(new ArrayList<>());
        gridAdapter.bindToRecyclerView(rvModuleGrid);
        gridAdapter.setNewData(getChildList(item));

        if (item.isParentItem()) {
            gridAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.ivModuleStatus) {
                        handleParentItemClick(gridAdapter.getData().get(position), gridAdapter);
                    }
                }
            });
        } else {
            //只有我的应用可以拖拽
            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(gridAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(rvModuleGrid);
            gridAdapter.enableDragItem(itemTouchHelper, R.id.rlDragItem, true);
            gridAdapter.setOnItemDragListener(onItemDragListener);
            gridAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.ivModuleStatus) {
                        handleMyItemClick(gridAdapter.getData().get(position), gridAdapter);
                    }
                }
            });
        }

    }

    private void loadHeader() {
        if (myItemList == null) {
            return;
        }
        HomeChildBean header = new HomeChildBean();
        header.setParentItem(false);
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

    private OnItemDragListener onItemDragListener = new OnItemDragListener() {
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
        }
    };

    public void setNewData(List<HomeChildBean> data) {
        super.setNewData(data);
        loadHeader();
    }


    private List<HomeChildItem> getChildList(HomeChildBean item) {
        if (item == null || item.getChildList() == null) {
            return null;
        }
        List<HomeChildItem> homeChildItemList = item.getChildList();
        for (HomeChildItem homeChildItem : homeChildItemList) {
            if (homeChildItem == null) {
                continue;
            }
            for (HomeChildItem childItem : myItemList) {
                if (childItem == null) {
                    continue;
                }
                if (childItem.getTitle().equals(homeChildItem.getTitle())) {
                    homeChildItem.setSelect(true);
                }
            }
        }
        return homeChildItemList;
    }


    private void handleMyItemClick(HomeChildItem myChildItem, GridModuleAdapter adapter) {
        int index = -1;
        for (int i = 0; i < myItemList.size(); i++) {
            if (myItemList.get(i).getTitle().equals(myChildItem.getTitle())) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            ToastUtil.showFailed("未找到对应item");
            return;
        }
        myItemList.remove(index);
        List<HomeChildBean> parentHomeBeanList = new ArrayList<>();
        for (HomeChildBean parentItem : getData()) {
            if (parentItem.isParentItem()) {
                parentHomeBeanList.add(parentItem);
            }
        }
        for (HomeChildBean homeChildBean : parentHomeBeanList) {
            if (homeChildBean == null || homeChildBean.getChildList() == null) {
                return;
            }
            for (HomeChildItem homeChildItem : homeChildBean.getChildList()) {
                if (myChildItem.getTitle().equals(homeChildItem.getTitle())) {
                    //找到父条目中对应的item 并且需要将该条目置为“未选中状态”
                    homeChildItem.setSelect(false);
                }
            }
        }
        //等子列表的动画执行完毕后再刷新
        mHandler.postDelayed(this::notifyDataSetChanged, 500);
        adapter.getData().remove(index);
        adapter.notifyItemRemoved(index);
    }

    /**
     * 处理其他应用列表的item点击事件
     *
     * @param otherItem
     * @param adapter
     */
    private void handleParentItemClick(HomeChildItem otherItem, GridModuleAdapter adapter) {
        if (otherItem.isSelect()) {
            //什么也不做
        } else {
            myItemList.add(otherItem);
            HomeChildItem childItemCopy = otherItem.copy();
            childItemCopy.setSelect(true);
            childItemCopy.setParentGroup(false);
            //向我的应用列表中添加条目
            List<HomeChildItem> childList = getData().get(0).getChildList();
            childList.add(childItemCopy);
            adapter.notifyItemInserted(childList.size() - 1);
            //并且需要将该条目置为“已选中状态”
            otherItem.setSelect(true);
            notifyDataSetChanged();
        }
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }
}
