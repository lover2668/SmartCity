package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.library.frame.R;

import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月28日15:20
 * @Email: 971613168@qq.com
 */
public class ModuleManagerAdapter extends BaseQuickAdapter<HomeChildItem, BaseViewHolder> {
    private List<HomeChildItem> myItemList;

    public ModuleManagerAdapter(List<HomeChildItem> myItemList) {
        super(R.layout.item_module_grid_layout);
        this.myItemList = myItemList;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildItem item) {
        helper.setText(R.id.tvModuleName, item.getTitle());
        ImageView ivModuleStatus = helper.getView(R.id.ivModuleStatus);
        ImageView ivModuleIcon = helper.getView(R.id.ivModuleIcon);
        GlideManager.loadRoundImg(item.getIcon(), ivModuleIcon,5);
        if (item.isParentGroup()) {
            //全部应用
            for (HomeChildItem homeChildItem : myItemList) {
                if (homeChildItem.getTitle().equals(item.getTitle())) {
                    ivModuleStatus.setImageResource(R.mipmap.icon_app_select);
                } else {
                    ivModuleStatus.setImageResource(R.mipmap.icon_app_add);
                }
            }
            helper.setGone(R.id.ivModuleStatus, true);
        } else {
            for (HomeChildItem homeChildItem : myItemList) {
                if (homeChildItem.getTitle().equals(item.getTitle())) {
                    ivModuleStatus.setImageResource(R.mipmap.icon_app_remove);
                    helper.setGone(R.id.ivModuleStatus, true);
                } else {
                    helper.setGone(R.id.ivModuleStatus, false);
                }
            }
        }
    }
}
