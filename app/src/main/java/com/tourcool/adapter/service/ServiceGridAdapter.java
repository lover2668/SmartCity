package com.tourcool.adapter.service;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.TwoLevelChildBean;
import com.tourcool.library.frame.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月21日17:03
 * @Email: 971613168@qq.com
 */
public class ServiceGridAdapter extends BaseQuickAdapter<TwoLevelChildBean, BaseViewHolder> {
    public ServiceGridAdapter() {
        super(R.layout.item_service_group_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TwoLevelChildBean item) {

    }
}
