package com.tourcool.core.adapter;

import android.view.ViewGroup;

import com.tourcool.core.base.BaseItemTouchQuickAdapter;
import com.tourcool.core.base.BaseItemTouchViewHolder;
import com.tourcool.core.entity.WidgetEntity;
import com.tourcool.core.helper.RadiusViewHelper;
import com.aries.ui.view.radius.RadiusRelativeLayout;
import com.tourcool.smartcity.R;

import java.util.ArrayList;

/**
 * @Author: JenkinsZhou on 2018/8/10 9:51
 * @E-Mail: 971613168@qq.com
 * Function: 描述性条目适配器
 * Description:
 */
public class WidgetAdapter extends BaseItemTouchQuickAdapter<WidgetEntity, BaseItemTouchViewHolder> {

    public WidgetAdapter() {
        super(R.layout.item_widget, new ArrayList<>());
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, WidgetEntity item) {
        helper.setText(R.id.tv_titleWidget, item.title)
                .setText(R.id.tv_contentWidget, item.content);
        int marginSize = helper.itemView.getResources().getDimensionPixelSize(R.dimen.dp_margin_item);
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        margin.setMargins(marginSize, marginSize, marginSize,
                helper.getLayoutPosition() == getItemCount() - 1 ? marginSize : 0);
        RadiusViewHelper.getInstance().setRadiusViewAdapter(((RadiusRelativeLayout) helper.itemView).getDelegate());
    }

}
