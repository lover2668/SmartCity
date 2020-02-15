package com.tourcool.adapter.parking;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.parking.CarInfo;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月14日19:31
 * @Email: 971613168@qq.com
 */
public class MyCarAdapter extends BaseQuickAdapter<CarInfo, BaseViewHolder> {
    public MyCarAdapter() {
        super(R.layout.item_parking_my_car);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, @NonNull CarInfo carInfo) {
        helper.setText(R.id.tvCarPlateNumber, carInfo.getCarNum());
        helper.setText(R.id.tvCarType, carInfo.getCarType());
        helper.addOnClickListener(R.id.ivUnbind);
       /* switch (carInfo.getCarType()) {
            case   CAR_TYPE_LARGE :

                break;
            case   CAR_TYPE_SMALL :
                helper.setText(R.id.tvCarType, "小型车");
                break;
                default:
                    helper.setText(R.id.tvCarType, "未知");
                    break;

        }*/

    }
}
