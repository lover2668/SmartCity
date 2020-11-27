package com.tourcool.adapter.driverlicense;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.driver.DriverAgainstDetail;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description : 违章明细适配器
 * @company :途酷科技
 * @date 2020年11月27日14:16
 * @Email: 971613168@qq.com
 */
public class DriverAgainstAdapter extends BaseQuickAdapter<DriverAgainstDetail, BaseViewHolder> {

    public DriverAgainstAdapter() {
        super(R.layout.item_driver_license_against);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DriverAgainstDetail item) {
        helper.setText(R.id.tvAgainstDate, item.getDate());
        helper.setText(R.id.tvHandleResult, item.getDate());
        helper.setText(R.id.tvAgainstLocate, item.getArea());
        helper.setText(R.id.tvAgainstCity, item.getWzcity());
        helper.setText(R.id.tvAgainstHandleDept, item.getCjjg());
        helper.setText(R.id.tvAgainstReason, item.getAct());
        helper.setText(R.id.tvAgainstScore, "扣分 " + item.getFen());
        helper.setText(R.id.tvAgainstMoney, "罚款 " + item.getMoney());
    }
}
