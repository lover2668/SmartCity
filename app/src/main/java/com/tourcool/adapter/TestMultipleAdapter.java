package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.home.HomeChildBean;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.library.frame.R;

import java.util.List;

import static com.tourcool.bean.home.HomeChildItem.ITEM_TYPE_CONTENT;
import static com.tourcool.bean.home.HomeChildItem.ITEM_TYPE_TITLE;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月02日11:33
 * @Email: 971613168@qq.com
 */
public class TestMultipleAdapter extends BaseMultiItemQuickAdapter<HomeChildBean, BaseViewHolder> {

    public TestMultipleAdapter(List<HomeChildBean> data) {
        super(data);
        //标题布局
        addItemType(ITEM_TYPE_TITLE, R.layout.item_group_name_layout);
        //内容布局
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_module_grid_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildBean item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_TITLE:
                helper.setText(R.id.tvGroupName, item.getTitle());
                break;
            case ITEM_TYPE_CONTENT:
                helper.setText(R.id.tvModuleName, item.getTitle());
                ImageView ivModuleStatus = helper.getView(R.id.ivModuleStatus);
                helper.addOnClickListener(R.id.ivModuleStatus);
                ImageView ivModuleIcon = helper.getView(R.id.ivModuleIcon);
                GlideManager.loadRoundImg(item.getIcon(), ivModuleIcon, 5);
                HomeChildItem childItem = item.getChildList().get(helper.getLayoutPosition());
                if (childItem.isParentGroup()) {
                    //全部应用
                    if (childItem.isSelect()) {
                        ivModuleStatus.setImageResource(R.mipmap.icon_app_select);
                    } else {
                        ivModuleStatus.setImageResource(R.mipmap.icon_app_add);
                    }
                } else {
                    ivModuleStatus.setImageResource(R.mipmap.icon_app_remove);
                }
                break;
        }
    }
}
