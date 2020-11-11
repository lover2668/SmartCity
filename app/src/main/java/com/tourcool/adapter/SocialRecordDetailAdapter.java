package com.tourcool.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.util.StringUtil;
import com.tourcool.bean.social.SocialDetail;
import com.tourcool.smartcity.R;

public class SocialRecordDetailAdapter extends BaseQuickAdapter<SocialDetail, BaseViewHolder> {
    public SocialRecordDetailAdapter() {
        super(R.layout.item_social_insurance_record);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SocialDetail item) {
        helper.setText(R.id.tvPayDate, StringUtil.getNotNullValueLine(item.getJyrq()));
        helper.setText(R.id.tvPayBaseLine, StringUtil.getNotNullValueLine(item.getJfjs()));
        helper.setText(R.id.tvPayPersonal, StringUtil.getNotNullValueLine(item.getGrjn()));
        helper.setText(R.id.tvPayCompany, StringUtil.getNotNullValueLine(item.getDwjn()));
        helper.setText(R.id.tvPayTotalAmount, StringUtil.getNotNullValueLine(item.getJnzje()));

    }
}
