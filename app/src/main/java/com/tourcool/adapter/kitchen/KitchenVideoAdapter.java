package com.tourcool.adapter.kitchen;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.kitchen.KitchenLiveInfo;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月05日22:18
 * @Email: 971613168@qq.com
 */
public class KitchenVideoAdapter extends BaseQuickAdapter<KitchenLiveInfo, BaseViewHolder> {
    private static final int STATUS_ON_lINE = 1;

    public KitchenVideoAdapter() {
        super(R.layout.item_bright_kitchen_video_detail_one);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, KitchenLiveInfo item) {
        if(item == null){
            return;
        }
        if(item.getLayoutType() == 0){
            helper.setGone(R.id.llTypeOne,false);
            helper.setGone(R.id.llTypeTwo,true);
        }else {
            helper.setGone(R.id.llTypeOne,true);
            helper.setGone(R.id.llTypeTwo,false);
        }
        helper.setText(R.id.tvUploaderName,"来自："+item.getDeviceSerial());
        helper.setText(R.id.tvCameraDesc,item.getCameraName());
        helper.setText(R.id.tvUploaderNameTwo,item.getDeviceSerial());
        helper.setText(R.id.tvCameraDescTwo,item.getCameraName());
           ImageView ivThumbnail =   helper.getView(R.id.ivThumbnail);
        ImageView ivThumbnailTwo =   helper.getView(R.id.ivThumbnailTypeTwo);
        switch (item.getOnlineStatus()) {
            case STATUS_ON_lINE:
                ivThumbnail.setImageResource(R.mipmap.ic_video_live_on_line);
                ivThumbnailTwo.setImageResource(R.mipmap.ic_video_live_on_line);
                helper.setGone(R.id.ivPlay,true);
                break;
            default:
                ivThumbnail.setImageResource(R.mipmap.ic_video_live_off_line);
                ivThumbnailTwo.setImageResource(R.mipmap.ic_video_live_off_line);
                helper.setGone(R.id.ivPlay,false);
                break;
        }
    }
}
