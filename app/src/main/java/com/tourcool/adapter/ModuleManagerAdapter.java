package com.tourcool.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.util.ToastUtil;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.helper.drag.OnDragVHListener;
import com.tourcool.library.frame.R;

import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月28日15:20
 * @Email: 971613168@qq.com
 */
public class ModuleManagerAdapter extends BaseQuickAdapter<HomeChildItem, MyViewHolder> {
    private List<HomeChildItem> myItemList;
    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;

    private ItemTouchHelper mItemTouchHelper;

    public ModuleManagerAdapter(List<HomeChildItem> myItemList) {
        super(R.layout.item_module_grid_layout);
        this.myItemList = myItemList;
    }

 /*   @Override
    protected void convert(@NonNull ModuleManagerAdapter.MyViewHolder helper, HomeChildItem item) {

    }*/

    @Override
    protected void convert(@NonNull MyViewHolder helper, HomeChildItem item) {
        helper.setText(R.id.tvModuleName, item.getTitle());
        ImageView ivModuleStatus = helper.getView(R.id.ivModuleStatus);
        ImageView ivModuleIcon = helper.getView(R.id.ivModuleIcon);
        GlideManager.loadRoundImg(item.getIcon(), ivModuleIcon, 5);
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






