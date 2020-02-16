package com.tourcool.adapter.parking;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.parking.ParingRecord;
import com.tourcool.core.util.DateUtil;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月16日11:28
 * @Email: 971613168@qq.com
 */
public class ParkingRecordAdapter extends BaseQuickAdapter<ParingRecord, BaseViewHolder> {
    public ParkingRecordAdapter() {
        super(R.layout.item_parking_pay_arrears);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ParingRecord item) {
        if(item == null){
            return;
        }
        helper.setChecked(R.id.cBoxParkingRecord,item.isSelect());
        helper.setText(R.id.tvPayAmount,item.getFeeAmount());
        helper.setText(R.id.tvParkingLocate,item.getParkingLot());
        helper.setText(R.id.tvParkingTime, DateUtil.formatDateTimeChinese(item.getParkTime()));
    }
}
