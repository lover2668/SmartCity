package com.tourcool.adapter.service;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.CategoryServiceBean;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月21日16:12
 * @Email: 971613168@qq.com
 */
public class CategoryServiceNameAdapter extends BaseQuickAdapter<CategoryServiceBean, BaseViewHolder> {

    public CategoryServiceNameAdapter() {
        super(R.layout.item_category_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CategoryServiceBean item) {
        helper.setText(R.id.tvCategoryName, item.getCategoryName());
        RelativeLayout relativeLayout = helper.getView(R.id.rlBackground);
        TextView tvClassifyName = helper.getView(R.id.tvCategoryName);
        if (item.isSelected()) {
            tvClassifyName.setTextColor(TourCooUtil.getColor(R.color.redD10303));
            relativeLayout.setBackground(TourCooUtil.getDrawable(R.drawable.tab));
        } else {
            tvClassifyName.setTextColor(TourCooUtil.getColor(R.color.grayA2A2A2));
            relativeLayout.setBackground(TourCooUtil.getDrawable(R.color.black));
        }
    }
}
