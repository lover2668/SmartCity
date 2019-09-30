package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.MatrixBean;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :矩阵适配器
 * @company :翼迈科技股份有限公司
 * @date 2019年08月19日23:03
 * @Email: 971613168@qq.com
 */
public class MatrixOldAdapter extends BaseQuickAdapter<MatrixBean, BaseViewHolder> {

    public MatrixOldAdapter() {
        super(R.layout.item_matrix_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MatrixBean item) {
        ImageView imageView = helper.getView(R.id.ivMatrixIcon);
        GlideManager.loadCircleImg(R.mipmap.leak_canary_icon, imageView);
        helper.setText(R.id.tvMatrixIconName, item.getMatrixName());

    }


}
