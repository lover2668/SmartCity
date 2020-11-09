package com.tourcool.adapter.social;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.core.entity.social.SocialDetail;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年11月06日17:40
 * @Email: 971613168@qq.com
 */
public class SocialRecordDetailAdapter extends BaseQuickAdapter<SocialDetail, BaseViewHolder> {

    public SocialRecordDetailAdapter() {
        super(R.layout.item_social_insurance_record);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SocialDetail item) {

    }
}
