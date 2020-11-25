package com.tourcool.adapter.express;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.bean.express.ExpressInfo;
import com.tourcool.smartcity.R;

/**
 * @author :JenkinsZhou
 * @description : 物流进度
 * @company :途酷科技
 * @date 2020年11月25日19:55
 * @Email: 971613168@qq.com
 */
public class ExpressStepAdapter extends BaseQuickAdapter<ExpressInfo, BaseViewHolder> {
    public ExpressStepAdapter() {
        super(R.layout.item_express_step);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ExpressInfo item) {
        helper.setText(R.id.tvExpressInfo, item.getRemark());
        helper.setText(R.id.tvExpressDate, item.getDatetime());
    }
}
