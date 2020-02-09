package com.tourcool.adapter.kitchen;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.kitchen.KitchenGroup;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月05日19:33
 * @Email: 971613168@qq.com
 */
public class KitchenDeviceAdapter extends BaseQuickAdapter<KitchenGroup, BaseViewHolder> {
    public KitchenDeviceAdapter() {
        super(R.layout.item_bright_kitchen_device_info);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, KitchenGroup item) {
            if(item == null){
                return;
            }
//        TextView tvVideoCount =  helper.getView(R.id.tvVideoCount);
        TextView tvVideoName =  helper.getView(R.id.tvVideoName);
        ImageView ivFolder = helper.getView(R.id.ivFolder);
        tvVideoName.setText(item.getGroupName());
            if(item.isSelect()){
                tvVideoName.setTextColor(TourCooUtil.getColor(R.color.blue5593FF));
                ivFolder.setImageResource(R.mipmap.ic_kitchen_folder_select);
//                tvVideoCount.setTextColor(TourCooUtil.getColor(R.color.blue5593FF));
            }else {
                tvVideoName.setTextColor(TourCooUtil.getColor(R.color.black333333));
                ivFolder.setImageResource(R.mipmap.ic_kitchen_folder);
//                tvVideoCount.setTextColor(TourCooUtil.getColor(R.color.black333333));
            }
       /* List<KitchenLiveInfo> kitchenLiveInfoList = item.getChildrenList();
            if(kitchenLiveInfoList == null){
                return;
            }
            int onLineCount = 0;
        for (KitchenLiveInfo kitchenLiveInfo : kitchenLiveInfoList) {
            if(kitchenLiveInfo != null&&kitchenLiveInfo.getOnlineStatus() == STATUS_ON_LINE){
                onLineCount++;
            }
        }
        String onLineInfo = "("+onLineCount+"/"+kitchenLiveInfoList.size()+")";
        tvVideoCount.setText(onLineInfo);*/
    }

    @NonNull
    @Override
    public List<KitchenGroup> getData() {
        return super.getData();
    }
}
