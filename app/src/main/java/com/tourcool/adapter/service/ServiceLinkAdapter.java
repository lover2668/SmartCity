package com.tourcool.adapter.service;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.frame.library.core.widget.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.frame.library.core.widget.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月22日11:26
 * @Email: 971613168@qq.com
 */
public class ServiceLinkAdapter implements ILinkagePrimaryAdapterConfig {
    private Context mContext;
    private static final int SPAN_COUNT_FOR_GRID_MODE = 2;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_service_category_layout;
    }

    @Override
    public int getGroupTitleViewId() {
        return R.id.tvCategoryName;
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_group;
    }

    @Override
    public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
        RelativeLayout rlBackground = holder.getView(R.id.rlBackground);
        TextView tvTitle = ((TextView) holder.mGroupTitle);
        tvTitle.setText(title);
        tvTitle.setTextColor(ContextCompat.getColor(mContext,
                selected ? R.color.black333333 : R.color.grayA2A2A2));
        tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        tvTitle.setFocusable(selected);
        tvTitle.setFocusableInTouchMode(selected);
        tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_NONE_MODE : MARQUEE_REPEAT_NONE_MODE);
        rlBackground.setBackground(selected ? TourCooUtil.getDrawable(R.drawable.tab): TourCooUtil.getDrawable(R.color.grayF5F5F5) );

    }

    @Override
    public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {

    }
}
