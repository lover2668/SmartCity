package com.tourcool.core.adapter;


import androidx.annotation.NonNull;

import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.util.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcool.library.frame.R;


/**
 * @author :JenkinsZhou
 * @description :系统消息适配器
 * @company :途酷科技
 * @date 2019年03月18日13:59
 * @Email: 971613168@qq.com
 */
public class SystemMsgAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public SystemMsgAdapter() {
        super(R.layout.item_system_msg);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageBean item) {
        helper.setText(R.id.tvMsgTime, DateUtil.getTimeStringChinaToDay(item.getCreateTime()+""));
        helper.setText(R.id.tvMsgContent, item.getMessage());
        if (item.getReadStatus() == 1) {
            helper.setVisible(R.id.tvRedDot, false);
        } else {
            helper.setVisible(R.id.tvRedDot, true);
        }

    }
}
