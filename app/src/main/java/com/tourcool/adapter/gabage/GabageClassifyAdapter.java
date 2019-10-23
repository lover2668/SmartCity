package com.tourcool.adapter.gabage;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.garbage.GabageEntity;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年10月23日15:59
 * @Email: 971613168@qq.com
 */
public class GabageClassifyAdapter extends BaseQuickAdapter<GabageEntity, BaseViewHolder> {

    public GabageClassifyAdapter() {
        super(R.layout.item_two_level_layout);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, GabageEntity item) {

    }
}
