package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.aries.library.fast.demo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.TwoLevelBean;
import com.tourcool.bean.TwoLevelChildBean;
import com.tourcool.core.util.TourCoolUtil;

/**
 * @author :JenkinsZhou
 * @description :二级列表子适配器
 * @company :途酷科技
 * @date 2019年08月20日17:14
 * @Email: 971613168@qq.com
 */
public class TwoLevelChildAdapter extends BaseQuickAdapter<TwoLevelChildBean, BaseViewHolder> {

    public TwoLevelChildAdapter() {
        super(R.layout.item_recyclerview_child_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TwoLevelChildBean item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvChildItemTitle, TourCoolUtil.getNotNullValueLine(item.getChildItemTitle()));
        helper.setText(R.id.tvChildItemDesc, TourCoolUtil.getNotNullValue(item.getChildItemDesc()));
        ImageView imageView = helper.getView(R.id.ivChildItemIcon);
        GlideManager.loadRoundImg(item.getChildItemIcon(), imageView, 2, R.mipmap.img_placeholder_car, true);

    }
}
