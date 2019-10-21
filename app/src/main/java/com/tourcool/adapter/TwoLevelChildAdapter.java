package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.bean.screen.Channel;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :二级列表子适配器
 * @company :途酷科技
 * @date 2019年08月20日17:14
 * @Email: 971613168@qq.com
 */
public class TwoLevelChildAdapter extends BaseQuickAdapter<Channel, BaseViewHolder> {

    public TwoLevelChildAdapter() {
        super(R.layout.item_recyclerview_child_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Channel item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvChildItemTitle, TourCooUtil.getNotNullValueLine(item.getTitle()));
        helper.setText(R.id.tvChildItemDesc, TourCooUtil.getNotNullValue(item.getDescription()));
        ImageView imageView = helper.getView(R.id.ivChildItemIcon);
        GlideManager.loadRoundImg(item.getIcon(), imageView, 2, R.mipmap.img_placeholder_car, true);

    }
}
