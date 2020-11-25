package com.tourcool.adapter.constellation;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.constellation.ConstellationInfo;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description : 星座适配器
 * @company :途酷科技
 * @date 2020年11月25日15:32
 * @Email: 971613168@qq.com
 */
public class ConstellationAdapter extends BaseQuickAdapter<ConstellationInfo, BaseViewHolder> {

    public ConstellationAdapter() {
        super(R.layout.item_constellation);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ConstellationInfo item) {
        GlideManager.loadImg(item.getImageIcon(), helper.getView(R.id.ivConstellationInfo));
        helper.setText(R.id.tvConstellationName, item.getName());
        helper.setText(R.id.tvConstellationMonth, item.getMonth());
    }
}
