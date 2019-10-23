package com.tourcool.adapter.gabage;

import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.TabSelectEntity;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月23日15:40
 * @Email: 971613168@qq.com
 */
public class TabSelectAdapter extends BaseQuickAdapter<TabSelectEntity, BaseViewHolder> {
    public TabSelectAdapter() {
        super(R.layout.item_category_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TabSelectEntity item) {
        if (item == null) {
            return;
        }
        RelativeLayout rlBackground = helper.getView(R.id.rlBackground);
        rlBackground.setBackground(item.isSelect() ? TourCooUtil.getDrawable(R.mipmap.ic_tab_select) : TourCooUtil.getDrawable(R.color.grayF5F5F5));
    }
}
