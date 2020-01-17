package com.tourcool.adapter;


import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.MatrixBean;
import com.tourcool.smartcity.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月23日13:50
 * @Email: 971613168@qq.com
 */
public class MineMenuAdapter extends BaseMultiItemQuickAdapter<MatrixBean, BaseViewHolder> {
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_LINE = 2;

    public MineMenuAdapter(List<MatrixBean> data) {
        super(data);
        addItemType(TYPE_ITEM, R.layout.item_mine_function_list);
        addItemType(TYPE_LINE, R.layout.item_mine_function_line);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MatrixBean item) {
        switch (helper.getItemViewType()) {
            case TYPE_ITEM:
                helper.setText(R.id.mineMenuName, item.getMatrixName());
                ImageView imageView = helper.getView(R.id.ivItemIcon);
                imageView.setImageResource(item.getMatrixIconResourcesId());
                break;
            case TYPE_LINE:

                break;
        }


    }
}
