package com.tourcool.adapter.service;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;
import com.frame.library.core.widget.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.bean.ServiceGroupItem;
import com.tourcool.library.frame.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月22日13:42
 * @Email: 971613168@qq.com
 */
public class ServiceSecondLinkAdapter implements ILinkageSecondaryAdapterConfig<ServiceGroupItem.ItemInfo> {
    private Context mContext;

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getGridLayoutId() {
        return 0;
    }

    @Override
    public int getLinearLayoutId() {
        return R.layout.item_service_group_layout;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.layout_item_header;
    }

    @Override
    public int getFooterLayoutId() {
        return 0;
    }

    @Override
    public int getHeaderTextViewId() {
        return R.id.secondary_header;
    }

    @Override
    public int getSpanCountOfGridMode() {
        return 0;
    }

    @Override
    public void onBindViewHolder(LinkageSecondaryViewHolder holder, BaseGroupedItem<ServiceGroupItem.ItemInfo> item) {
        ImageView imageView = holder.getView(R.id.ivGroupIcon);
        GlideManager.loadImg(R.mipmap.icon_service_group, imageView);
        RecyclerView childRecyclerView = holder.getView(R.id.rvItemGrid);
        MatrixAdapter adapter = new MatrixAdapter();
        //二级布局为网格布局
        childRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter.bindToRecyclerView(childRecyclerView);
        if (item.info instanceof ServiceGroupItem.ItemInfo) {
            adapter.setNewData(item.info.getMatrixBeanList());
        }

    }

    @Override
    public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder, BaseGroupedItem<ServiceGroupItem.ItemInfo> item) {
        ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
    }

    @Override
    public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder, BaseGroupedItem<ServiceGroupItem.ItemInfo> item) {

    }
}
