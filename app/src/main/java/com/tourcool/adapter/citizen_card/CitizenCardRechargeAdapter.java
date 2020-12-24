package com.tourcool.adapter.citizen_card;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.util.StringUtil;
import com.tourcool.bean.recharge.RechargeEntity;
import com.tourcool.core.util.ResourceUtil;
import com.tourcool.smartcity.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description : JenkinsZhou
 * @company :途酷科技
 * @date 2020年12月24日14:03
 * @Email: 971613168@qq.com
 */
public class CitizenCardRechargeAdapter extends BaseQuickAdapter<RechargeEntity, BaseViewHolder> {

    public CitizenCardRechargeAdapter(@Nullable List<RechargeEntity> data) {
        super(R.layout.item_citizen_card_recharge_amount, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeEntity item) {
        helper.setText(R.id.tvRechargeMoney, StringUtil.formatDouble(item.rechargeMoney));
        TextView yuan = helper.getView(R.id.yuan);
        if (item.selected) {
            helper.setBackgroundRes(R.id.llRechargeMoney, R.drawable.selector_bg_radius_5_blue_hollow);
            helper.setTextColor(R.id.tvRechargeMoney, ResourceUtil.getColor(R.color.colorPrimary));
            yuan.setTextColor( ResourceUtil.getColor(R.color.colorPrimary));
        } else {
            helper.setBackgroundRes(R.id.llRechargeMoney, R.drawable.selector_bg_radius_5_gray_hollow);
            helper.setTextColor(R.id.tvRechargeMoney, ResourceUtil.getColor(R.color.grayCommon));
            yuan.setTextColor( ResourceUtil.getColor(R.color.grayCommon));
        }
    }


}
