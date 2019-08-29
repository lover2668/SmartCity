package com.tourcool.adapter.drag;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.library.frame.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月29日10:30
 * @Email: 971613168@qq.com
 */
public class GridModuleAdapter extends BaseItemDraggableAdapter<HomeChildItem, BaseViewHolder> {
    public GridModuleAdapter(List<HomeChildItem> data) {
        super(R.layout.item_module_grid_layout, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildItem item) {
        helper.setText(R.id.tvModuleName, item.getTitle());
        ImageView ivModuleStatus = helper.getView(R.id.ivModuleStatus);
        helper.addOnClickListener(R.id.ivModuleStatus);
        ImageView ivModuleIcon = helper.getView(R.id.ivModuleIcon);
        GlideManager.loadRoundImg(item.getIcon(), ivModuleIcon, 5);
        if (item.isParentGroup()) {
            //全部应用
            if (item.isSelect()) {
                ivModuleStatus.setImageResource(R.mipmap.icon_app_select);
            } else {
                ivModuleStatus.setImageResource(R.mipmap.icon_app_add);
            }
        } else {
            ivModuleStatus.setImageResource(R.mipmap.icon_app_remove);
        }
    }


}