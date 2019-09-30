package com.tourcool.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frame.library.core.manager.GlideManager;
import com.tourcool.bean.home.HomeChildItem;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月26日16:06
 * @Email: 971613168@qq.com
 */
public class MatrixAdapter extends BaseQuickAdapter<HomeChildItem, BaseViewHolder> {

    public MatrixAdapter() {
        super(R.layout.item_matrix_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeChildItem item) {
        ImageView imageView = helper.getView(R.id.ivMatrixIcon);
        GlideManager.loadCircleImg(item.getIcon(), imageView);
        helper.setText(R.id.tvMatrixIconName, item.getTitle());

    }
}
