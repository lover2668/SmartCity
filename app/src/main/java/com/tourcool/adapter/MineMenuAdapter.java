package com.tourcool.adapter;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.MatrixBean;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月23日13:50
 * @Email: 971613168@qq.com
 */
public class MineMenuAdapter extends BaseQuickAdapter<MatrixBean, BaseViewHolder> {
    public MineMenuAdapter(){
        super(R.layout.item_mine_function_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MatrixBean item) {
        helper.setText(R.id.mineMenuName,item.getMatrixName());

    }
}
